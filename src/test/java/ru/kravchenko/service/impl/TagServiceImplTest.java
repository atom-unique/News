package ru.kravchenko.service.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.kravchenko.exception.EntityNotFoundException;
import ru.kravchenko.model.Tag;
import ru.kravchenko.service.TagService;
import ru.kravchenko.servlet.dto.CommentDto;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class TagServiceImplTest extends CommentDto {

    @Container
    public static final MySQLContainer<?> container =
            new MySQLContainer<>("mysql:8.3.0")
                    .withDatabaseName("news")
                    .withUsername("root")
                    .withPassword("root")
                    .withInitScript("db-migration.sql");

    @Mock
    TagService tagService;

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
        tagService = new TagServiceImpl(connection);
    }

    @Test
    void isContainerRunning() {
        assertTrue(container.isRunning());
        assertEquals("news", container.getDatabaseName());
    }

    @Test
    void findTagByIdTest() {
        Tag tag = tagService.findById(1L);
        Assertions.assertEquals("Наука", tag.getName());
    }

    @Test
    void saveTagTest() {
        Tag tag = new Tag();
        tag.setName("Робототехника");
        tagService.saveTag(tag);
        Assertions.assertEquals("Робототехника", tagService.findById(5L).getName());
    }

    @Test
    void removeTagTest() {
        Tag tag = new Tag();
        tag.setName("Робототехника");
        tagService.saveTag(tag);
        Assertions.assertEquals("Робототехника", tagService.findById(5L).getName());
        tagService.removeTag(5L);
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> {
                    tagService.findById(5L);
                });
    }
}