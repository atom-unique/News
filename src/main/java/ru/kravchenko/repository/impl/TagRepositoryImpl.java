package ru.kravchenko.repository.impl;

import ru.kravchenko.connection.DataSource;
import ru.kravchenko.exception.ExecuteQueryException;
import ru.kravchenko.exception.ModelMappingException;
import ru.kravchenko.model.Tag;
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
    private static final String FIND_ALL_BY_NEWS_ID = "SELECT id, name FROM tag t INNER JOIN news_tag nt ON t.id=nt.tag_id WHERE news_id = ?";
    private static final String CREATE = "INSERT INTO tag (name) VALUES (?)";
    private static final String REMOVE = "DELETE FROM tag WHERE id = ?";
    private final TagMapper tagMapper;
    private final Connection connection;
    private final Class<?> thisClass;
    private final Class<?> modelClass;

    public TagRepositoryImpl() {
        this(DataSource.getConnection());
    }

    public TagRepositoryImpl(Connection connection) {
        this.tagMapper = new TagMapperImpl();
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

    @Override
    public List<Tag> findAll(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_NEWS_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Tag> tagList = new ArrayList<>();
            try {
                while (resultSet.next()) {
                    Tag tag = new Tag();
                    try {
                        tag.setId(resultSet.getLong("id"));
                        tag.setName(resultSet.getString("name"));
                    } catch (SQLException exception) {
                        throw new ModelMappingException(modelClass);
                    }
                    tagList.add(tag);
                }
                return tagList;
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
                target.add(tagMapper.map(resultSet));
            }
            return target;
        } catch (SQLException exception) {
            throw new ModelMappingException(modelClass);
        }
    }
}
