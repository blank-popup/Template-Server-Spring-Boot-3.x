package org.duckdns.ahamike.rollbook.process.board.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SchemaPerTenantConnectionProvider implements MultiTenantConnectionProvider<String> {
    @Autowired
    private DataSource dataSource;
    // @Value("${spring.datasource.url}")
    // private String datasourceUrl;
    // private String DEFAULT_TENANT;

    // @PostConstruct
    // public void init() {
    //     DEFAULT_TENANT = datasourceUrl.split("/")[3];
    // }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = dataSource.getConnection();
        // if (tenantIdentifier == null) {
        //     connection.setCatalog(TenantContext.getDefaultTenant());
        // }
        // else {
        //     connection.setCatalog(tenantIdentifier); // MariaDB uses 'catalog' for schema switching
        // }
        connection.setCatalog(tenantIdentifier); // MariaDB uses 'catalog' for schema switching
        log.error("$$$$$$$$$ tenantIdentifier: {}", tenantIdentifier);
        return connection;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        // connection.setCatalog("test_db_rollbook");
        // connection.setCatalog(datasourceUrl.split("/")[3]);
        // connection.setCatalog(DEFAULT_TENANT);
        connection.setCatalog(TenantContext.getDefaultTenant());
        connection.close();
    }

    @Override public boolean supportsAggressiveRelease() { return false; }
    @Override public boolean isUnwrappableAs(Class<?> unwrapType) { return false; }
    @Override public <T> T unwrap(Class<T> unwrapType) { return null; }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
        // // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'releaseAnyConnection'");
    }
}
