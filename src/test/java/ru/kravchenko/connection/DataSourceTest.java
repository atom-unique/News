package ru.kravchenko.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;

@Testcontainers
class DataSourceTest {

    @Mock
    private Connection connection;

    @Container
    public static final MySQLContainer<?> container =
            new MySQLContainer<>("mysql:8.3.0")
                    .withDatabaseName("news")
                    .withUsername("root")
                    .withPassword("root")
                    .withInitScript("db-migration.sql");

    @BeforeEach
    void setUp() throws SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(container.getJdbcUrl());
        hikariConfig.setUsername(container.getUsername());
        hikariConfig.setPassword(container.getPassword());
        var dataSource = new HikariDataSource(hikariConfig);
        connection = dataSource.getConnection();
    }

    @Test
    void getConnection() throws SQLException {
        Assertions.assertEquals("root@172.17.0.1", connection.getMetaData().getUserName());
        Assertions.assertEquals("MySQL Connector/J", connection.getMetaData().getDriverName());
    }
}