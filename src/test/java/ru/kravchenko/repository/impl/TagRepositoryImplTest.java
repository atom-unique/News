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
import ru.kravchenko.model.Tag;
import ru.kravchenko.servlet.dto.CommentDto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class TagRepositoryImplTest extends CommentDto {

    @Container
    public static final MySQLContainer<?> container =
            new MySQLContainer<>("mysql:8.3.0")
                    .withDatabaseName("news")
                    .withUsername("root")
                    .withPassword("root")
                    .withInitScript("db-migration.sql");

    @Mock
    private TagRepositoryImpl tagRepository;

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
        tagRepository = new TagRepositoryImpl(connection);
    }

    @Test
    void isContainerRunning() {
        assertTrue(container.isRunning());
        assertEquals("news", container.getDatabaseName());
    }

    @Test
    void findOneTest() {
        Tag tag = tagRepository.findOne(2L).orElseGet(null);
        Assertions.assertEquals("Техника", tag.getName());
    }

    @Test
    void createTest() {
        Assertions.assertFalse(tagRepository.findOne(5L).isPresent());
        Tag tag = new Tag();
        tag.setName("Новый тег");
        tagRepository.create(tag);
        tag = tagRepository.findOne(5L).orElseGet(null);
        Assertions.assertEquals("Новый тег", tag.getName());
    }

    @Test
    void removeTest() {
        Assertions.assertTrue(tagRepository.findOne(1L).isPresent());
        tagRepository.remove(1L);
        Assertions.assertFalse(tagRepository.findOne(1L).isPresent());
    }

    @Test
    void findAllNewsByTagIdTest() {
        List<News> newsList = tagRepository.findAllNewsByTagId(1L);
        Assertions.assertEquals(2, newsList.size());
    }
}