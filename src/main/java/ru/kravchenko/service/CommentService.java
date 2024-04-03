package ru.kravchenko.service;

import ru.kravchenko.model.Comment;

import java.util.List;

public interface CommentService {

    Comment findById(Long id);

    List<Comment> findAllComment(Long newsId);

    Comment saveComment(Comment comment);

    void removeComment(Long id);
}
