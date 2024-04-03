package ru.kravchenko.repository.mapper.impl;

import ru.kravchenko.exception.ModelMappingException;
import ru.kravchenko.model.Comment;
import ru.kravchenko.repository.mapper.CommentMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentMapperImpl implements CommentMapper {

    private final Class<?> modelClass;

    public CommentMapperImpl() {
        modelClass = Comment.class;
    }

    @Override
    public Comment map(ResultSet resultSet) {
        Comment comment = new Comment();
        try {
            comment.setId(resultSet.getLong("id"));
            comment.setNewsId(resultSet.getLong("news_id"));
            comment.setAuthor(resultSet.getString("author"));
            comment.setDateTime(resultSet.getTimestamp("date_time").toLocalDateTime());
            comment.setText(resultSet.getString("text"));
        } catch (SQLException exception) {
            throw new ModelMappingException(modelClass);
        }
        return comment;
    }
}
