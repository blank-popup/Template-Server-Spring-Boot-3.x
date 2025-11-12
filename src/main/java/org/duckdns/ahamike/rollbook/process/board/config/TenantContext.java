package org.duckdns.ahamike.rollbook.process.board.config;

import org.springframework.beans.factory.annotation.Value;

public class TenantContext {
    // @Value("#{'${spring.datasource.url}'.split(',')[3]}")
    // @Value("${spring.datasource.url}")
    // private static String datasourceUrl;
    private static final String DEFAULT_TENANT = "test_db_rollbook";

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static void setCurrentTenant(String tenant) {
        CURRENT_TENANT.set(tenant);
    }

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }

    public static String getDefaultTenant() {
        return DEFAULT_TENANT;
    }
}
