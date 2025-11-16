package org.duckdns.ahamike.rollbook.config.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// @Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilterCustom extends OncePerRequestFilter {
    private final ProviderJwt providerJwt;
    private final ProviderApiKey providerApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.debug("\nExecute Order :\n"
            + "1. AuthenticationFilterCustom.doFilter\n"
            + "2. UserDetailsServiceCustom.loadUserByUsername\n"
            + "3. UserDetailsCustom.getRoles\n"
            + "4. ServiceAuthorization.check"
        );

        while (true) {
            String method = request.getMethod();
            String contextPath = request.getContextPath();
            String requestURI = request.getRequestURI();
            if (method == null || (contextPath != null && requestURI != null && requestURI.equals(contextPath + "/error")) == true) {
                break;
            }
            log.debug("[method] requestURI : [{}] {}", method, requestURI);

            String jwt = providerJwt.resolveJwt((HttpServletRequest) request);
            if (StringUtils.hasText(jwt)) {
                Jws<Claims> information = providerJwt.getInformationOfJwt(jwt);
                if (information == null) {
                    break;
                }

                Authentication authentication = providerJwt.getAuthentication(information, jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                break;
            }

            String apiKey = providerApiKey.resolveApiKey((HttpServletRequest) request);
            if (StringUtils.hasText(apiKey)) {
                List<String> information = providerApiKey.getInformationOfApiKey(apiKey);
                if (information == null) {
                    break;
                }

                Authentication authentication = providerApiKey.getAuthentication(information, apiKey);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                break;
            }

            log.warn("There is no valid authentication : {}", requestURI);
            break;
        }

        filterChain.doFilter(request, response);
    }
}
