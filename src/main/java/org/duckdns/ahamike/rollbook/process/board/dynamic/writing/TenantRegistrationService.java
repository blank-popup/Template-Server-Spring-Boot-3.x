package org.duckdns.ahamike.rollbook.process.board.dynamic.writing;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.duckdns.ahamike.rollbook.process.board.config.TenantContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantRegistrationService {
    // @Value("${spring.datasource.url}")
    // private String datasourceUrl;
    // private String DEFAULT_TENANT;

    // @PostConstruct
    // public void init() {
    //     DEFAULT_TENANT = datasourceUrl.split("/")[3];
    // }

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void registerNewTenant(String tenantId) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            // 1. Create schema (database)
            statement.execute("CREATE DATABASE IF NOT EXISTS " + tenantId);
            System.out.println("✅ Created schema for tenant: " + tenantId);

            // 2. Register tenant in master
            // jdbcTemplate.update(String.format("INSERT INTO %s.tb_tenant (tenant_id) VALUES ('%s')", "test_db_rollbook", tenantId));
            // jdbcTemplate.update(String.format("INSERT INTO %s.tb_tenant (tenant_id) VALUES ('%s')", datasourceUrl.split("/")[3], tenantId));
            // jdbcTemplate.update(String.format("INSERT INTO %s.tb_tenant (tenant_id) VALUES ('%s')", DEFAULT_TENANT, tenantId));
            jdbcTemplate.update(String.format("INSERT INTO %s.tb_tenant (tenant_id) VALUES ('%s')", TenantContext.getDefaultTenant(), tenantId));
        }

        // 3. (Optional) Initialize schema by creating tables
        initializeSchema(tenantId);
    }

    private void initializeSchema(String tenantId) {
        TenantContext.setCurrentTenant(tenantId);
        try {
            // Touch a repository or trigger Hibernate schema creation
            jdbcTemplate.execute("USE " + tenantId);
            // jdbcTemplate.execute("""
            //     CREATE TABLE IF NOT EXISTS tb_user2 (
            //         id BIGINT AUTO_INCREMENT PRIMARY KEY,
            //         username VARCHAR(100)
            //     )
            // """);
            jdbcTemplate.execute(String.format("CREATE TABLE IF NOT EXISTS tb_user2 like %s.tb_user2", TenantContext.getDefaultTenant()));
            System.out.println("✅ Initialized tables for tenant: " + tenantId);
        } finally {
            TenantContext.clear();
        }
    }
}
