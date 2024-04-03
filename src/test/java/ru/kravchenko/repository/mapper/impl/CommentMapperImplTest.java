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
import ru.kravchenko.model.Comment;
import ru.kravchenko.repository.mapper.CommentMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Testcontainers
class CommentMapperImplTest {

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
    CommentMapper commentMapper = new CommentMapperImpl();

    private final Class<?> modelClass = Comment.class;

    @BeforeEach
    void setUp() throws SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(container.getJdbcUrl());
        hikariConfig.setUsername(container.getUsername());
        hikariConfig.setPassword(container.getPassword());
        var dataSource = new HikariDataSource(hikariConfig);
        connection = dataSource.getConnection();
    }

    @Test
    void mapTest() {
        List<Comment> target = new ArrayList<>();
        Comment comment;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT id, news_id, author, date_time, text FROM comment WHERE id = 1"
             )) {
            while (resultSet.next()) {
                target.add(commentMapper.map(resultSet));
            }
            comment = target.isEmpty() ? null : target.get(0);
        } catch (SQLException exception) {
            throw new ModelMappingException(modelClass);
        }
        Assertions.assertNotNull(comment);
        Assertions.assertEquals("автор 100", comment.getAuthor());
        Assertions.assertEquals("текст 100", comment.getText());
    }
}