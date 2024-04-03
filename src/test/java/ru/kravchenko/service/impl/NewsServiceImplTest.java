package ru.kravchenko.service.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.kravchenko.model.News;
import ru.kravchenko.service.NewsService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class NewsServiceImplTest {

    @Container
    public static final MySQLContainer<?> container =
            new MySQLContainer<>("mysql:8.3.0")
                    .withDatabaseName("news")
                    .withUsername("root")
                    .withPassword("root")
                    .withInitScript("db-migration.sql");

    @Mock
    NewsService newsService;

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
        newsService = new NewsServiceImpl(connection);
    }

    @Test
    void isContainerRunning() {
        assertTrue(container.isRunning());
        assertEquals("news", container.getDatabaseName());
    }

    @Test
    void findByIdTest() {
        News news = newsService.findById(2L);
        Assertions.assertEquals("Заголовок 200", news.getTitle());
        Assertions.assertEquals("Автор 200", news.getAuthor());
        Assertions.assertEquals("Текст 200", news.getText());
    }

    @Test
    @Disabled("run separately")
    void findAllNewsTest() {
        List<News> newsList = newsService.findAllNews();
        Assertions.assertEquals(4, newsList.size());
    }

    @Test
    void saveNewsTestNewNews() {
        List<News> newsList = newsService.findAllNews();
        assertEquals(4, newsList.size());
        News news = new News();
        news.setTitle("Заголовок 500");
        news.setAuthor("Автор 500");
        LocalDateTime dateTime = LocalDateTime.now();
        news.setDateTime(dateTime);
        news.setText("Текст 500");
        newsService.saveNews(news);
        newsList = newsService.findAllNews();
        assertEquals(5, newsList.size());
    }

    @Test
    @Disabled("run separately")
    void saveNewsTestExistingNews() {
        List<News> newsList = newsService.findAllNews();
        assertEquals(4, newsList.size());
        News news = newsService.findById(1L);
        Assertions.assertEquals("Заголовок 100", news.getTitle());
        Assertions.assertEquals("Автор 100", news.getAuthor());
        Assertions.assertEquals("Текст 100", news.getText());
        news.setTitle("Заголовок 1000");
        news.setAuthor("Автор 1000");
        news.setText("Текст 1000");
        newsService.saveNews(news);
        news = newsService.findById(1L);
        Assertions.assertEquals("Заголовок 1000", news.getTitle());
        Assertions.assertEquals("Автор 1000", news.getAuthor());
        Assertions.assertEquals("Текст 1000", news.getText());
    }

    @Test
    @Disabled("run separately")
    void removeNewsTest() {
        List<News> newsList = newsService.findAllNews();
        assertEquals(4, newsList.size());
        Assertions.assertNotNull(newsService.findById(1L));
        newsService.removeNews(1L);
        newsList = newsService.findAllNews();
        assertEquals(3, newsList.size());
    }
}