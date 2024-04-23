package ru.kravchenko.repository.impl;

import ru.kravchenko.connection.DataSource;
import ru.kravchenko.exception.ExecuteQueryException;
import ru.kravchenko.exception.ModelMappingException;
import ru.kravchenko.model.News;
import ru.kravchenko.model.Tag;
import ru.kravchenko.repository.CommentRepository;
import ru.kravchenko.repository.TagRepository;
import ru.kravchenko.repository.mapper.TagMapper;
import ru.kravchenko.repository.mapper.impl.TagMapperImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TagRepositoryImpl implements TagRepository {

    private static final String FIND_ONE = "SELECT id, name FROM tag WHERE id = ?";
    private static final String FIND_ALL_BY_TAG_ID = "SELECT id, title, author, date_time, text FROM news n INNER JOIN news_tag nt ON n.id=nt.news_id WHERE tag_id = ?";
    private static final String CREATE = "INSERT INTO tag (name) VALUES (?)";
    private static final String REMOVE = "DELETE FROM tag WHERE id = ?";
    private final CommentRepository commentRepository;
    private final TagMapper tagMapper;
    private final Connection connection;
    private final Class<?> thisClass;
    private final Class<?> modelClass;

    public TagRepositoryImpl() {
        this(new DataSource().getConnection());
    }

    public TagRepositoryImpl(Connection connection) {
        this.tagMapper = new TagMapperImpl();
        this.commentRepository = new CommentRepositoryImpl(connection);
        this.connection = connection;
        thisClass = this.getClass();
        modelClass = Tag.class;
    }

    @Override
    public Optional<Tag> findOne(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ONE)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Tag> list = buildTags(resultSet);
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        } catch (SQLException exception) {
            throw new ExecuteQueryException(thisClass, exception);
        }
    }

    @Override
    public Optional<Tag> create(Tag tag) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, tag.getName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            return resultSet.next() ? findOne(resultSet.getLong(1)) : Optional.empty();
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

    public List<News> findAllNewsByTagId(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_TAG_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<News> newsList = new ArrayList<>();
            try {
                while (resultSet.next()) {
                    News news = new News();
                    try {
                        news.setId(resultSet.getLong("id"));
                        news.setTitle(resultSet.getString("title"));
                        news.setAuthor(resultSet.getString("author"));
                        news.setDateTime(resultSet.getTimestamp("date_time").toLocalDateTime());
                        news.setText(resultSet.getString("text"));
                        news.setCommentList(commentRepository.findAll(news.getId()));
                    } catch (SQLException exception) {
                        throw new ModelMappingException(modelClass);
                    }
                    newsList.add(news);
                }
                return newsList;
            } catch (SQLException exception) {
                throw new ModelMappingException(modelClass);
            }
        } catch (SQLException exception) {
            throw new ExecuteQueryException(thisClass, exception);
        }
    }

    private List<Tag> buildTags(ResultSet resultSet) {
        List<Tag> target = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Tag tag = tagMapper.map(resultSet);
                tag.setNewsList(findAllNewsByTagId(tag.getId()));
                target.add(tag);
            }
            return target;
        } catch (SQLException exception) {
            throw new ModelMappingException(modelClass);
        }
    }
}
