package org.duckdns.ahamike.rollbook.config.security.tenant.setting;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.debug("$$$ Enter TenantFilter.doFilterInternal");

        // /contextPath/version/board/{tenantBelong}/{tenantName}/...
        String uri = request.getRequestURI();
        // must have contextPath otherwise index error
        TenantContext.setBoardBelongTenant(uri);
        String tenantName = TenantContext.getCurrentTenant();
        if (tenantName == null || tenantName.isBlank() == false) {
            filterChain.doFilter(request, response);
        }
        else {
            // Check Tenant Existence
            // CacheTenant cacheTenant = TenantSetting.getCacheTenant();
            TenantContext.setCurrentTenant(tenantName);

            try {
                filterChain.doFilter(request, response);
            } finally {
                TenantContext.clear();
            }
        }
    }
}
