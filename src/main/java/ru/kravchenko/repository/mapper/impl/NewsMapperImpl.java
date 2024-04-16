package ru.kravchenko.repository.mapper.impl;

import ru.kravchenko.exception.ModelMappingException;
import ru.kravchenko.model.News;
import ru.kravchenko.repository.CommentRepository;
import ru.kravchenko.repository.TagRepository;
import ru.kravchenko.repository.impl.CommentRepositoryImpl;
import ru.kravchenko.repository.impl.TagRepositoryImpl;
import ru.kravchenko.repository.mapper.NewsMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewsMapperImpl implements NewsMapper {

    private final CommentRepository commentRepository;
//    private final TagRepository tagRepository;
    private final Class<?> modelClass;

    public NewsMapperImpl() {
        this.commentRepository = new CommentRepositoryImpl();
//        this.tagRepository = new TagRepositoryImpl();
        modelClass = News.class;
    }

    public NewsMapperImpl(Connection connection) {
        this.commentRepository = new CommentRepositoryImpl(connection);
//        this.tagRepository = new TagRepositoryImpl(connection);
        modelClass = News.class;
    }

    @Override
    public News map(ResultSet resultSet) {
        News news = new News();
        try {
            news.setId(resultSet.getLong("id"));
            news.setTitle(resultSet.getString("title"));
            news.setAuthor(resultSet.getString("author"));
            news.setDateTime(resultSet.getTimestamp("date_time").toLocalDateTime());
            news.setText(resultSet.getString("text"));
            news.setCommentList(commentRepository.findAll(news.getId()));
//            news.setTagList(tagRepository.findAll(news.getId()));
        } catch (SQLException exception) {
            throw new ModelMappingException(modelClass);
        }
        return news;
    }
}
