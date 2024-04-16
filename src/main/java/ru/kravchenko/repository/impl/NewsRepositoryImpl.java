package ru.kravchenko.repository.impl;

import ru.kravchenko.connection.DataSource;
import ru.kravchenko.exception.ExecuteQueryException;
import ru.kravchenko.exception.ModelMappingException;
import ru.kravchenko.model.News;
import ru.kravchenko.model.Tag;
import ru.kravchenko.repository.NewsRepository;
import ru.kravchenko.repository.mapper.NewsMapper;
import ru.kravchenko.repository.mapper.impl.NewsMapperImpl;

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

public class NewsRepositoryImpl implements NewsRepository {

    private static final String FIND_ONE = "SELECT id, title, author, date_time, text FROM news WHERE id = ?";
    private static final String FIND_ALL = "SELECT id, title, author, date_time, text FROM news";
    private static final String FIND_ALL_BY_NEWS_ID = "SELECT id, name FROM tag t INNER JOIN news_tag nt ON t.id=nt.tag_id WHERE news_id = ?";
    private static final String CREATE = "INSERT INTO news (title, author, date_time, text) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE news SET title = ?, author = ?, date_time = ?, text = ? WHERE id = ?";
    private static final String REMOVE = "DELETE FROM news WHERE id = ?";
    private final NewsMapper newsMapper;
    private final Connection connection;
    private final Class<?> thisClass;
    private final Class<?> modelClass;

    public NewsRepositoryImpl() {
        this(DataSource.getConnection());
    }

    public NewsRepositoryImpl(Connection connection) {
        this.connection = connection;
        this.newsMapper = new NewsMapperImpl();
        thisClass = this.getClass();
        modelClass = News.class;
    }

    @Override
    public Optional<News> findOne(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ONE)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<News> list = buildNews(resultSet);
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        } catch (SQLException exception) {
            throw new ExecuteQueryException(thisClass, exception);
        }
    }

    @Override
    public List<News> findAll() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL);) {
            return buildNews(resultSet);
        } catch (SQLException exception) {
            throw new ExecuteQueryException(thisClass, exception);
        }
    }

    public List<Tag> findAllTagsByNewsId(Long id) {
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

    @Override
    public Optional<News> create(News news) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getAuthor());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(4, news.getText());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            return resultSet.next() ? findOne(resultSet.getLong(1)) : Optional.empty();
        } catch (SQLException exception) {
            throw new ExecuteQueryException(thisClass, exception);
        }
    }

    @Override
    public Optional<News> update(News news) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getAuthor());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(news.getDateTime()));
            preparedStatement.setString(4, news.getText());
            preparedStatement.setLong(5, news.getId());
            preparedStatement.executeUpdate();
            return findOne(news.getId());
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

    private List<News> buildNews(ResultSet resultSet) {
        List<News> target = new ArrayList<>();
        try {
            while (resultSet.next()) {
                News news = newsMapper.map(resultSet);
                news.setTagList(findAllTagsByNewsId(news.getId()));
                target.add(news);
            }
            return target;
        } catch (SQLException exception) {
            throw new ModelMappingException(modelClass);
        }
    }
}
