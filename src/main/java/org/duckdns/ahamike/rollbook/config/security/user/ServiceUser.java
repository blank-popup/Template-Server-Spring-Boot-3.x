package org.duckdns.ahamike.rollbook.config.security.user;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.duckdns.ahamike.rollbook.config.constant.ReturnCode;
import org.duckdns.ahamike.rollbook.config.exception.ExceptionBusiness;
import org.duckdns.ahamike.rollbook.config.redis.ServiceRedis;
import org.duckdns.ahamike.rollbook.config.security.ProviderJwt;
import org.duckdns.ahamike.rollbook.config.security.UserDetailsCustom;
import org.duckdns.ahamike.rollbook.config.security.UserDetailsServiceCustom;
import org.duckdns.ahamike.rollbook.config.security.role.RepositoryRole;
import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.duckdns.ahamike.rollbook.process.project.tag.RepositoryTag;
import org.duckdns.ahamike.rollbook.table.EntityRole;
import org.duckdns.ahamike.rollbook.table.EntityTag;
import org.duckdns.ahamike.rollbook.table.EntityUser;
import org.duckdns.ahamike.rollbook.util.client.ClientInfo;
import org.duckdns.ahamike.rollbook.util.validator.NullString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServiceUser {
    private final RepositoryUser repositoryUser;
    private final RepositoryRole repositoryRole;
    private final RepositoryTag repositoryTag;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceCustom userDetailsService;
    private final ProviderJwt providerJwt;
    private final ServiceRedis serviceRedis;

    @Value("${spring.data.redis.group0_user}")
    private String group0_user;

    public GlobalResponse<EntityUser> signUp(RequestSignUp request) {
        if (NullString.isNullOrWhitespace(request.getUsername()) == true
                || NullString.isNullOrWhitespace(request.getPassword()) == true
                || NullString.isNullOrWhitespace(request.getName()) == true) {
            throw new ExceptionBusiness(ReturnCode.NO_ESSENTIAL_DATA, "Username, password or name is not valid");
        }

        repositoryUser.findByUsername(request.getUsername())
                .ifPresent((existingUser) -> {
                    throw new ExceptionBusiness(ReturnCode.DATA_ALREADY_EXISTS, "Username already exists");
                });

        String tagName = request.getTag();
        if (NullString.isNotNullAndWhitespace(tagName) == true) {
            throw new ExceptionBusiness(ReturnCode.NOT_NULL_AND_WHITESPACE, "Tag is not valid");
        }

        if (NullString.isNotNullAndNotWhitespace(tagName) == true) {
            List<EntityTag> listTag = repositoryTag.findByName(tagName);
            if (listTag.size() > 0) {
                throw new ExceptionBusiness(ReturnCode.DATA_ALREADY_EXISTS, "Tag already exists");
            }
        }

        EntityTag entityTag = null;
        if (tagName != null) {
            entityTag = repositoryTag.save(new EntityTag(tagName));
        }

        Set<EntityRole> setRole = repositoryRole.findAllByNameIn(request.getRoles());
        if (setRole.size() != request.getRoles().size()) {
            throw new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Role does not exist");
        }

        String password = passwordEncoder.encode(request.getPassword());
        EntityUser entity = new EntityUser(
            request.getUsername(),
            password,
            entityTag,
            request.getName(),
            request.getEmail(),
            request.getPhone(),
            setRole
        );
        
        EntityUser response = repositoryUser.save(entity);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<ResponseSignIn> signIn(RequestSignIn request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }

        EntityUser user = repositoryUser.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
        if (passwordEncoder.matches(request.getPassword(), user.getPassword()) == false) {
            throw new UsernameNotFoundException("Invalid username or password");
        }

        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = ClientInfo.getRemoteIP(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        log.info("Username: [{}], IP Address: [{}], User-Agent : [{}]", user.getUsername(), ip, userAgent);
        String token = providerJwt.createJwt(user.getUsername(), user.getRoles().stream().map(EntityRole::getName).collect(Collectors.toSet()), ip, userAgent);
        serviceRedis.setValue(group0_user, user.getUsername(), token, user, serviceRedis.getExpireSeconds());

        String usernameKey = serviceRedis.buildKey(group0_user, user.getUsername(), token);
        UserDetailsCustom userDetails = userDetailsService.loadUserByUsername(usernameKey);

        ResponseSignIn response = new ResponseSignIn();
        response.setId(userDetails.getId());
        response.setUsername(userDetails.getUsername());
        response.setToken(token);
        response.setName(userDetails.getName());
        response.setEmail(userDetails.getEmail());
        response.setPhone(userDetails.getPhone());
        response.setTag(userDetails.getTag());
        response.setRoles(userDetails.getRoles());

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<ResponseSignIn> signOut(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (auth != null && auth.isAuthenticated() == true && "anonymousUser".equals(auth.getPrincipal()) == false) {
            username = auth.getName();
        }

        String authorization = request.getHeader("Authorization");
        String token = null;
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }

        Object user = serviceRedis.getValue(group0_user, username, token);
        if (user == null) {
            throw new ExceptionBusiness(ReturnCode.NOT_SIGNED_IN, "User not found in Redis");
        }

        Boolean removed = serviceRedis.removeValue(group0_user, username, token);

        if (removed == true) {
            ReturnCode code = ReturnCode.OK;
            return new GlobalResponse<>(
                    code.getCode(),
                    code.getMessage(),
                    code.getHttpStatus(),
                    null
            );
        }
        else {
            throw new ExceptionBusiness(ReturnCode.FAIL_TO_REMOVE, "Fail to remove user");
        }
    }

    public GlobalResponse<List<EntityUser>> getUsers(String username, String name, String tag) {
        if (username == null && name == null && tag == null) {
            throw new ExceptionBusiness(ReturnCode.EVERY_CONDITION_IS_NULL, "Username, password and name are null all");
        }

        List<EntityUser> listEntity = repositoryUser.findByNotNullUsernameNameTag(username, name, tag);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                listEntity
        );
    }
}
