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
import ru.kravchenko.exception.ExecuteQueryException;
import ru.kravchenko.exception.ModelMappingException;
import ru.kravchenko.model.Comment;
import ru.kravchenko.repository.CommentRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class CommentRepositoryImplTest {

    @Container
    public static final MySQLContainer<?> container =
            new MySQLContainer<>("mysql:8.3.0")
                    .withDatabaseName("news")
                    .withUsername("root")
                    .withPassword("root")
                    .withInitScript("db-migration.sql");

    @Mock
    private CommentRepository repository;

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
        repository = new CommentRepositoryImpl(connection);
    }

    @Test
    void isContainerRunning() {
        assertTrue(container.isRunning());
        assertEquals("news", container.getDatabaseName());
    }

    @Test
    void findOneTest() {
        Comment comment = repository.findOne(1L).orElseGet(null);
        Assertions.assertEquals("автор 100", comment.getAuthor());
        Assertions.assertEquals("текст 100", comment.getText());
    }

    @Test
    void findAllTest() {
        List<Comment> commentList = repository.findAll(1L);
        Assertions.assertEquals(2, commentList.size());
    }

    @Test
    void createTest() {
        Assertions.assertFalse(repository.findOne(5L).isPresent());
        Comment comment = new Comment();
        comment.setNewsId(1L);
        comment.setAuthor("автор 500");
        LocalDateTime dateTime = LocalDateTime.now();
        comment.setDateTime(dateTime);
        comment.setText("текст 500");
        repository.create(comment);
        Comment newComment = repository.findOne(5L).orElseGet(null);
        Assertions.assertEquals("автор 500", newComment.getAuthor());
        Assertions.assertEquals("текст 500", newComment.getText());
    }

    @Test
    void updateTest() {
        Comment comment = repository.findOne(1L).orElseGet(null);
        Assertions.assertEquals("автор 100", comment.getAuthor());
        Assertions.assertEquals("текст 100", comment.getText());
        comment.setText("текст 1000");
        repository.update(comment);
        comment = repository.findOne(1L).orElseGet(null);
        Assertions.assertEquals("текст 1000", comment.getText());
    }

    @Test
    void removeTest() {
        List<Comment> commentList = repository.findAll(1L);
        Assertions.assertEquals(2, commentList.size());
        Assertions.assertTrue(repository.findOne(1L).isPresent());
        repository.remove(1L);
        commentList = repository.findAll(1L);
        Assertions.assertEquals(1, commentList.size());
        Assertions.assertFalse(repository.findOne(1L).isPresent());
    }

    @Test
    void executeQueryExceptionTest() {
        Assertions.assertThrows(ExecuteQueryException.class,
                () -> {
                    throw new ExecuteQueryException(this.getClass(), new RuntimeException());
                });
    }

    @Test
    void modelMappingExceptionTest() {
        Assertions.assertThrows(ModelMappingException.class,
                () -> {
                    throw new ModelMappingException(this.getClass());
                });
    }
}