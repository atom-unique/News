package ru.kravchenko.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class DataSource {

    private HikariConfig config;
    private HikariDataSource dataSource;

    public DataSource() {
        this.config = new HikariConfig(
                Objects.requireNonNull(
                                Thread.currentThread()
                                        .getContextClassLoader()
                                        .getResource("datasource.properties"))
                        .getPath()
        );
        this.dataSource = new HikariDataSource(config);
    }

    private DataSource(HikariConfig config) {
        this.dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
