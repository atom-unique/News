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
import ru.kravchenko.exception.EntityCreateException;
import ru.kravchenko.exception.EntityNotFoundException;
import ru.kravchenko.exception.EntityUpdateException;
import ru.kravchenko.model.Comment;
import ru.kravchenko.service.CommentService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class CommentServiceImplTest {

    @Container
    public static final MySQLContainer<?> container =
            new MySQLContainer<>("mysql:8.3.0")
                    .withDatabaseName("news")
                    .withUsername("root")
                    .withPassword("root")
                    .withInitScript("db-migration.sql");

    @Mock
    CommentService commentService;

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
        commentService = new CommentServiceImpl(connection);
    }

    @Test
    void isContainerRunning() {
        assertTrue(container.isRunning());
        assertEquals("news", container.getDatabaseName());
    }

    @Test
    void findByIdTest() {
        Comment comment = commentService.findById(4L);
        Assertions.assertEquals("автор 400", comment.getAuthor());
        Assertions.assertEquals("текст 400", comment.getText());
    }

    @Test
    void findAllCommentTest() {
        List<Comment> commentList = commentService.findAllComment(1L);
        Assertions.assertEquals(2, commentList.size());
    }

    @Test
    void saveCommentTestNewComment() {
        List<Comment> commentList = commentService.findAllComment(1L);
        assertEquals(2, commentList.size());
        Comment comment = new Comment();
        comment.setNewsId(1L);
        comment.setAuthor("автор 500");
        LocalDateTime dateTime = LocalDateTime.now();
        comment.setDateTime(dateTime);
        comment.setText("текст 500");
        commentService.saveComment(comment);
        commentList = commentService.findAllComment(1L);
        assertEquals(3, commentList.size());
        commentService.removeComment(5L);
    }

    @Test
    void entityCreateExceptionTest() {
        Assertions.assertThrows(EntityCreateException.class,
                () -> {
                    throw new EntityCreateException(this.getClass());
                });
    }

    @Test
    void entityNotFoundExceptionTest() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> {
                    throw new EntityNotFoundException(this.getClass(), 1L);
                });
    }

    @Test
    void entityUpdateExceptionTest() {
        Assertions.assertThrows(EntityUpdateException.class,
                () -> {
                    throw new EntityUpdateException(this.getClass());
                });
    }

    @Test
    void saveCommentTestExistingComment() {
        List<Comment> commentList = commentService.findAllComment(2L);
        assertEquals(2, commentList.size());
        Comment comment = commentService.findById(2L);
        assertEquals("текст 200", commentService.findById(2L).getText());
        comment.setText("текст 2000");
        commentService.saveComment(comment);
        commentList = commentService.findAllComment(2L);
        assertEquals(2, commentList.size());
        assertEquals("текст 2000", commentService.findById(2L).getText());
    }

    @Test
    void removeCommentTest() {
        List<Comment> commentList = commentService.findAllComment(1L);
        assertEquals(2, commentList.size());
        Assertions.assertNotNull(commentService.findById(1L));
        commentService.removeComment(1L);
        commentList = commentService.findAllComment(1L);
        assertEquals(1, commentList.size());
    }
}