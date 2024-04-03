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
        Comment comment = commentService.findById(1L);
        Assertions.assertEquals("автор 100", comment.getAuthor());
        Assertions.assertEquals("текст 100", comment.getText());
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
    }

    @Test
    @Disabled("run separately")
    void saveCommentTestExistingComment() {
        List<Comment> commentList = commentService.findAllComment(1L);
        assertEquals(2, commentList.size());
        Comment comment = commentService.findById(1L);
        assertEquals("текст 100", commentService.findById(1L).getText());
        comment.setText("текст 1000");
        commentService.saveComment(comment);
        commentList = commentService.findAllComment(1L);
        assertEquals(2, commentList.size());
        assertEquals("текст 1000", commentService.findById(1L).getText());
    }

    @Test
    @Disabled("run separately")
    void removeCommentTest() {
        List<Comment> commentList = commentService.findAllComment(1L);
        assertEquals(2, commentList.size());
        Assertions.assertNotNull(commentService.findById(1L));
        commentService.removeComment(1L);
        commentList = commentService.findAllComment(1L);
        assertEquals(1, commentList.size());
    }
}