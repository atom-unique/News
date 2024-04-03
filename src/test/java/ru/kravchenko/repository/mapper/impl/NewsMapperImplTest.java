package ru.kravchenko.repository.mapper.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.kravchenko.exception.ModelMappingException;
import ru.kravchenko.model.News;
import ru.kravchenko.repository.mapper.NewsMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Testcontainers
class NewsMapperImplTest {

    @Container
    public static final MySQLContainer<?> container =
            new MySQLContainer<>("mysql:8.3.0")
                    .withDatabaseName("news")
                    .withUsername("root")
                    .withPassword("root")
                    .withInitScript("db-migration.sql");

    @Mock
    private Connection connection;

    @Mock
    NewsMapper newsMapper;

    private final Class<?> modelClass = News.class;

    @BeforeEach
    void setUp() throws SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(container.getJdbcUrl());
        hikariConfig.setUsername(container.getUsername());
        hikariConfig.setPassword(container.getPassword());
        var dataSource = new HikariDataSource(hikariConfig);
        connection = dataSource.getConnection();
        newsMapper = new NewsMapperImpl(connection);
    }

    @Test
    void mapTest() {
        List<News> target = new ArrayList<>();
        News news;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT id, title, author, date_time, text FROM news WHERE id = 1"
             )) {
            while (resultSet.next()) {
                target.add(newsMapper.map(resultSet));
            }
            news = target.isEmpty() ? null : target.get(0);
        } catch (SQLException exception) {
            throw new ModelMappingException(modelClass);
        }
        Assertions.assertNotNull(news);
        Assertions.assertEquals("Заголовок 100", news.getTitle());
        Assertions.assertEquals("Автор 100", news.getAuthor());
        Assertions.assertEquals("Текст 100", news.getText());
    }
}