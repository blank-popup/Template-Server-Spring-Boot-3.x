package org.duckdns.ahamike.rollbook.process.board.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver<String> {
    // @Value("${spring.datasource.url}")
    // private String datasourceUrl;
    // private String DEFAULT_TENANT;

    // @PostConstruct
    // public void init() {
    //     DEFAULT_TENANT = datasourceUrl.split("/")[3];
    // }

    @Override
    public String resolveCurrentTenantIdentifier() {
        // Usually read from ThreadLocal, JWT, or request header
        String tenant = TenantContext.getCurrentTenant();
        // return tenant != null ? tenant : "test_db_rollbook";
        // return tenant != null ? tenant : datasourceUrl.split("/")[3];
        // return tenant != null ? tenant : DEFAULT_TENANT;
        return tenant != null ? tenant : TenantContext.getDefaultTenant();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
