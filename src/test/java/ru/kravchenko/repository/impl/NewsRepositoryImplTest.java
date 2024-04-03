package ru.kravchenko.repository.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.kravchenko.model.News;
import ru.kravchenko.repository.NewsRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class NewsRepositoryImplTest {

    @Container
    public static final MySQLContainer<?> container =
            new MySQLContainer<>("mysql:8.3.0")
                    .withDatabaseName("news")
                    .withUsername("root")
                    .withPassword("root")
                    .withInitScript("db-migration.sql");

    @Mock
    private NewsRepository repository;

    @Mock
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(container.getJdbcUrl());
        hikariConfig.setUsername(container.getUsername());
        hikariConfig.setPassword(container.getPassword());
        var dataSource = new HikariDataSource(hikariConfig);
        connection = dataSource.getConnection();
        repository = new NewsRepositoryImpl(connection);
    }

    @Test
    void isContainerRunning() {
        assertTrue(container.isRunning());
        assertEquals("news", container.getDatabaseName());
    }

    @Test
    void findOneTest() {
        News news = repository.findOne(2L).orElseGet(null);
        Assertions.assertEquals("Заголовок 200", news.getTitle());
        Assertions.assertEquals("Автор 200", news.getAuthor());
        Assertions.assertEquals("Текст 200", news.getText());
    }

    @Test
    void findAllTest() {
        List<News> newsList = repository.findAll();
        Assertions.assertEquals(4, newsList.size());
    }

    @Test
    void createTest() {
        Assertions.assertFalse(repository.findOne(5L).isPresent());
        News news = new News();
        news.setTitle("Заголовок 500");
        news.setAuthor("Автор 500");
        LocalDateTime dateTime = LocalDateTime.now();
        news.setDateTime(dateTime);
        news.setText("Текст 500");
        repository.create(news);
        news = repository.findOne(5L).orElseGet(null);
        Assertions.assertEquals("Автор 500", news.getAuthor());
        Assertions.assertEquals("Текст 500", news.getText());
    }

    @Test
    void updateTest() {
        News news = repository.findOne(1L).orElseGet(null);
        Assertions.assertEquals("Автор 100", news.getAuthor());
        Assertions.assertEquals("Текст 100", news.getText());
        news.setTitle("Заголовок 1000");
        news.setAuthor("Автор 1000");
        news.setText("Текст 1000");
        repository.update(news);
        news = repository.findOne(1L).orElseGet(null);
        Assertions.assertEquals("Заголовок 1000", news.getTitle());
        Assertions.assertEquals("Автор 1000", news.getAuthor());
        Assertions.assertEquals("Текст 1000", news.getText());
    }

    @Test
    void removeTest() {
        List<News> newsList = repository.findAll();
        Assertions.assertEquals(4, newsList.size());
        Assertions.assertTrue(repository.findOne(1L).isPresent());
        repository.remove(1L);
        newsList = repository.findAll();
        Assertions.assertEquals(3, newsList.size());
        Assertions.assertFalse(repository.findOne(1L).isPresent());
    }
}