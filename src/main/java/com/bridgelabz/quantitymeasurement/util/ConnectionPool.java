
package com.bridgelabz.quantitymeasurement.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;

/**
 * UC16: JDBC connection pool using HikariCP.
 */
public final class ConnectionPool implements AutoCloseable {

    private final HikariDataSource dataSource;

    public ConnectionPool(ApplicationConfig config) {

        this(
                config.getDatabaseUrl(),
                config.getDatabaseUsername(),
                config.getDatabasePassword(),
                config.getInt("database.pool.maximumSize", 5),
                config.getInt("database.pool.minimumIdle", 1),
                config.getInt("database.pool.connectionTimeout", 10000)
        );
    }

    /**
     * Useful for configuring isolated H2 databases in tests.
     */
    public ConnectionPool(
            String url,
            String username,
            String password,
            int maximumSize,
            int minimumIdle,
            long timeoutMs) {

        if (url.startsWith("jdbc:h2:file:./data/")) {
            try {
                Files.createDirectories(Paths.get("data"));
            } catch (IOException exception) {
                throw new IllegalStateException(
                        "Unable to create database directory",
                        exception);
            }
        }

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        hikariConfig.setMaximumPoolSize(maximumSize);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setConnectionTimeout(timeoutMs);

        hikariConfig.setPoolName("QuantityMeasurementPool");

        dataSource = new HikariDataSource(hikariConfig);
    }

    /**
     * Obtains a pooled connection.
     * Closing the connection returns it to the pool.
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public String getStatistics() {

        var pool = dataSource.getHikariPoolMXBean();

        if (pool == null) {
            return "Connection pool not initialized";
        }

        return "Active: " + pool.getActiveConnections()
                + ", Idle: " + pool.getIdleConnections()
                + ", Total: " + pool.getTotalConnections();
    }

    @Override
    public void close() {
        dataSource.close();
    }
}
