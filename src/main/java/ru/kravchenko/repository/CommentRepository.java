package ru.kravchenko.repository;

import ru.kravchenko.model.Comment;

import java.util.List;

public interface CommentRepository extends CommonRepository<Comment> {

    List<Comment> findAll(Long newsId);
}
