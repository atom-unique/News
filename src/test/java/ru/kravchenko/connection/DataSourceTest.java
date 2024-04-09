package ru.kravchenko.connection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.SQLException;

class DataSourceTest {

    @Test
    void getConnection() throws SQLException {
        Connection connection = DataSource.getConnection();
        Assertions.assertEquals("root@172.18.0.1", connection.getMetaData().getUserName());
        Assertions.assertEquals("MySQL Connector/J", connection.getMetaData().getDriverName());
    }
}