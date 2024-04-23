package ru.kravchenko.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private final String APP_CONF = "/datasource.properties";
    private HikariConfig config = new HikariConfig(APP_CONF);
    private HikariDataSource dataSource;

    public DataSource() {
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
