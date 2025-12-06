package org.duckdns.ahamike.rollbook.config.security.user;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.duckdns.ahamike.rollbook.config.redis.RedisService;
import org.duckdns.ahamike.rollbook.config.response.BusinessException;
import org.duckdns.ahamike.rollbook.config.response.GlobalResponse;
import org.duckdns.ahamike.rollbook.config.response.ReturnCode;
import org.duckdns.ahamike.rollbook.config.security.JwtProvider;
import org.duckdns.ahamike.rollbook.config.security.UserDetailsCustom;
import org.duckdns.ahamike.rollbook.config.security.UserDetailsServiceCustom;
import org.duckdns.ahamike.rollbook.config.security.role.RoleRepository;
import org.duckdns.ahamike.rollbook.process.project.tag.TagRepository;
import org.duckdns.ahamike.rollbook.table.RoleEntity;
import org.duckdns.ahamike.rollbook.table.TagEntity;
import org.duckdns.ahamike.rollbook.table.UserEntity;
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
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TagRepository tagRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceCustom userDetailsService;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    @Value("${spring.data.redis.group0_user}")
    private String group0_user;

    public GlobalResponse<UserEntity> signUp(RequestSignUp request) {
        if (NullString.isNullOrWhitespace(request.getUsername()) == true
                || NullString.isNullOrWhitespace(request.getPassword()) == true
                || NullString.isNullOrWhitespace(request.getName()) == true) {
            throw new BusinessException(ReturnCode.NO_ESSENTIAL_DATA, "Username, password or name is not valid");
        }

        userRepository.findByUsername(request.getUsername())
                .ifPresent((existingUser) -> {
                    throw new BusinessException(ReturnCode.DATA_ALREADY_EXISTS, "Username already exists");
                });

        String tagName = request.getTag();
        if (NullString.isNotNullAndWhitespace(tagName) == true) {
            throw new BusinessException(ReturnCode.NOT_NULL_AND_WHITESPACE, "Tag is not valid");
        }

        if (NullString.isNotNullAndNotWhitespace(tagName) == true) {
            List<TagEntity> tagList = tagRepository.findByName(tagName);
            if (tagList.size() > 0) {
                throw new BusinessException(ReturnCode.DATA_ALREADY_EXISTS, "Tag already exists");
            }
        }

        TagEntity entityTag = null;
        if (tagName != null) {
            entityTag = tagRepository.save(new TagEntity(tagName));
        }

        Set<RoleEntity> setRole = roleRepository.findAllByNameIn(request.getRoles());
        if (setRole.size() != request.getRoles().size()) {
            throw new BusinessException(ReturnCode.NO_SUCH_DATA, "Role does not exist");
        }

        String password = passwordEncoder.encode(request.getPassword());
        UserEntity entity = new UserEntity(
            request.getUsername(),
            password,
            entityTag,
            request.getName(),
            request.getEmail(),
            request.getPhone(),
            setRole
        );
        
        UserEntity response = userRepository.save(entity);

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

        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
        if (passwordEncoder.matches(request.getPassword(), user.getPassword()) == false) {
            throw new UsernameNotFoundException("Invalid username or password");
        }

        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = ClientInfo.getRemoteIP(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        log.info("Username: [{}], IP Address: [{}], User-Agent : [{}]", user.getUsername(), ip, userAgent);
        String token = jwtProvider.createJwt(user.getUsername(), user.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet()), ip, userAgent);
        redisService.setValue(group0_user, user.getUsername(), token, user, redisService.getExpireSeconds());

        String usernameKey = redisService.buildKey(group0_user, user.getUsername(), token);
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

        Object user = redisService.getValue(group0_user, username, token);
        if (user == null) {
            throw new BusinessException(ReturnCode.NOT_SIGNED_IN, "User not found in Redis");
        }

        Boolean removed = redisService.removeValue(group0_user, username, token);

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
            throw new BusinessException(ReturnCode.FAIL_TO_REMOVE, "Fail to remove user");
        }
    }

    public GlobalResponse<List<UserEntity>> getUsers(String username, String name, String tag) {
        if (username == null && name == null && tag == null) {
            throw new BusinessException(ReturnCode.EVERY_CONDITION_IS_NULL, "Username, password and name are null all");
        }

        List<UserEntity> entityList = userRepository.findByNotNullUsernameNameTag(username, name, tag);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                entityList
        );
    }
}
