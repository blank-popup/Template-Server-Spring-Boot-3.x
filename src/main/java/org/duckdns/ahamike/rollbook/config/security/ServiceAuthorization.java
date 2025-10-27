package org.duckdns.ahamike.rollbook.config.security;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.duckdns.ahamike.rollbook.config.security.endpoint.CacheEndpoint;
import org.duckdns.ahamike.rollbook.config.security.endpoint.InfoEndpoint;
import org.duckdns.ahamike.rollbook.util.client.ClientInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ServiceAuthorization implements AuthorizationManager<RequestAuthorizationContext> {
    @Autowired
    private CacheEndpoint cacheEndpoint;

    // @Cacheable("check")
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext requestContext) {
        Authentication auth = authenticationSupplier.get();
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetailsCustom == false) {
            return new AuthorizationDecision(false);
        }
        UserDetailsCustom userDetails = (UserDetailsCustom) principal;

        HttpServletRequest request = requestContext.getRequest();

        String ip = ClientInfo.getRemoteIP(request);
        String userAgent = request.getHeader("User-Agent");
        String method = request.getMethod();
        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        String uriWithoutContextPath = uri.substring(contextPath.length());
        String query = request.getQueryString();
        String uriWithoutContextPathWithQuery = (query == null) ? uriWithoutContextPath : (uriWithoutContextPath + "?" + query);
        if (method == null) {
            return new AuthorizationDecision(false);
        }
        log.debug("Authorization Request [{}] {} from {}, {}", method, uriWithoutContextPathWithQuery, ip, userAgent);




        String endpointName = null;
        Map<String, InfoEndpoint> mapEndpointRoles = cacheEndpoint.getMapEndpointRoles();
        for (String key : mapEndpointRoles.keySet()) {
            InfoEndpoint infoEndpoint = mapEndpointRoles.get(key);
            PathPattern pattern = infoEndpoint.getPattern();
            if (pattern.matches(PathContainer.parsePath(uriWithoutContextPath)) == true) {
                if (method != null && method.equals(infoEndpoint.getMethod()) == true) {
                    endpointName = key;
                    break;
                }
            } 
        }

        if (endpointName == null) {
            return new AuthorizationDecision(false);
        }

        List<String> requiredRoles = cacheEndpoint.getRoles(endpointName);
        if (requiredRoles.isEmpty()) {
            return new AuthorizationDecision(false);
        }

        boolean granted = userDetails.getRoles().stream().anyMatch(r -> requiredRoles.contains(r));

        return new AuthorizationDecision(granted);
    }
}
