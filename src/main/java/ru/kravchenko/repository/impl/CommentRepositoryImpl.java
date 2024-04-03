package ru.kravchenko.repository.impl;

import ru.kravchenko.connection.DataSource;
import ru.kravchenko.exception.ExecuteQueryException;
import ru.kravchenko.exception.ModelMappingException;
import ru.kravchenko.model.Comment;
import ru.kravchenko.repository.CommentRepository;
import ru.kravchenko.repository.mapper.CommentMapper;
import ru.kravchenko.repository.mapper.impl.CommentMapperImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentRepositoryImpl implements CommentRepository {

    private static final String FIND_ONE = "SELECT id, news_id, author, date_time, text FROM comment WHERE id = ?";
    private static final String FIND_ALL_BY_NEWS_ID = "SELECT id, news_id, author, date_time, text FROM comment WHERE news_id = ?";
    private static final String CREATE = "INSERT INTO comment (news_id, author, date_time, text) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE comment SET date_time = ?, text = ? WHERE id = ?";
    private static final String REMOVE = "DELETE FROM comment WHERE id = ?";
    private final CommentMapper commentMapper;
    private final Connection connection;
    private final Class<?> thisClass;
    private final Class<?> modelClass;

    public CommentRepositoryImpl() {
        this(DataSource.getConnection());
    }

    public CommentRepositoryImpl(Connection connection) {
        this.connection = connection;
        this.commentMapper = new CommentMapperImpl();
        thisClass = this.getClass();
        modelClass = Comment.class;
    }

    @Override
    public Optional<Comment> findOne(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ONE)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Comment> list = buildComment(resultSet);
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        } catch (SQLException exception) {
            throw new ExecuteQueryException(thisClass, exception);
        }
    }

    @Override
    public List<Comment> findAll(Long newsId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_NEWS_ID)) {
            preparedStatement.setLong(1, newsId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return buildComment(resultSet);
        } catch (SQLException exception) {
            throw new ExecuteQueryException(thisClass, exception);
        }
    }

    @Override
    public Optional<Comment> create(Comment comment) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, comment.getNewsId());
            preparedStatement.setString(2, comment.getAuthor());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(4, comment.getText());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            return resultSet.next() ? findOne(resultSet.getLong(1)) : Optional.empty();
        } catch (SQLException exception) {
            throw new ExecuteQueryException(thisClass, exception);
        }
    }

    @Override
    public Optional<Comment> update(Comment comment) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(comment.getDateTime()));
            preparedStatement.setString(2, comment.getText());
            preparedStatement.setLong(3, comment.getId());
            preparedStatement.executeUpdate();
            return findOne(comment.getId());
        } catch (SQLException exception) {
            throw new ExecuteQueryException(thisClass, exception);
        }
    }

    @Override
    public void remove(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(REMOVE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new ExecuteQueryException(thisClass, exception);
        }
    }

    private List<Comment> buildComment(ResultSet resultSet) {
        List<Comment> target = new ArrayList<>();
        try {
            while (resultSet.next()) {
                target.add(commentMapper.map(resultSet));
            }
            return target;
        } catch (SQLException exception) {
            throw new ModelMappingException(modelClass);
        }
    }
}
