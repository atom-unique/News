package ru.kravchenko.service.impl;

import ru.kravchenko.exception.EntityCreateException;
import ru.kravchenko.exception.EntityNotFoundException;
import ru.kravchenko.exception.EntityUpdateException;
import ru.kravchenko.model.Comment;
import ru.kravchenko.repository.CommentRepository;
import ru.kravchenko.repository.impl.CommentRepositoryImpl;
import ru.kravchenko.service.CommentService;

import java.sql.Connection;
import java.util.List;

public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final Class<?> modelClass;

    public CommentServiceImpl() {
        this.commentRepository = new CommentRepositoryImpl();
        this.modelClass = Comment.class;
    }

    public CommentServiceImpl(Connection connection) {
        this.commentRepository = new CommentRepositoryImpl(connection);
        this.modelClass = Comment.class;
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findOne(id).orElseThrow(
                () -> new EntityNotFoundException(modelClass, id)
        );
    }

    @Override
    public List<Comment> findAllComment(Long newsId) {
        List<Comment> commentList = commentRepository.findAll(newsId);
        return commentList.isEmpty() ? List.of() : commentList;
    }

    @Override
    public Comment saveComment(Comment comment) {
        if (comment.getId() != null && commentRepository.findOne(comment.getId()).isPresent()) {
            return commentRepository.update(comment).orElseThrow(
                    () -> new EntityUpdateException(modelClass)
            );
        } else {
            return commentRepository.create(comment).orElseThrow(
                    () -> new EntityCreateException(modelClass)
            );
        }
    }

    @Override
    public void removeComment(Long id) {
        commentRepository.remove(id);
    }
}
