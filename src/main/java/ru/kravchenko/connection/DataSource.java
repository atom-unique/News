package ru.kravchenko.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static final String APP_CONF = "/datasource.properties";
    private static HikariConfig config = new HikariConfig(APP_CONF);
    private static HikariDataSource dataSource;

    static {
        dataSource = new HikariDataSource(config);
    }

    private DataSource() {
    }

    public static Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
