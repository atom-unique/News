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
import ru.kravchenko.model.Tag;
import ru.kravchenko.repository.mapper.TagMapper;
import ru.kravchenko.servlet.dto.CommentDto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Testcontainers
class TagMapperImplTest extends CommentDto {

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
    TagMapper tagMapper = new TagMapperImpl();

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
    void map() {
        List<Tag> target = new ArrayList<>();
        Tag tag;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT id, name FROM tag WHERE id = 1"
             )) {
            while (resultSet.next()) {
                target.add(tagMapper.map(resultSet));
            }
            tag = target.isEmpty() ? null : target.get(0);
        } catch (SQLException exception) {
            throw new ModelMappingException(modelClass);
        }
        Assertions.assertNotNull(tag);
        Assertions.assertEquals("Наука", tag.getName());
    }
}