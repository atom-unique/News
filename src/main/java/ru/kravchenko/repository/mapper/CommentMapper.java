package ru.kravchenko.repository.mapper;

import ru.kravchenko.model.Comment;

import java.sql.ResultSet;

public interface CommentMapper {

    Comment map(ResultSet resultSet);
}
