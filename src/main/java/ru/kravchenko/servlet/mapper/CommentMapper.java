package ru.kravchenko.servlet.mapper;

import ru.kravchenko.model.Comment;
import ru.kravchenko.servlet.dto.IncomingCommentDto;
import ru.kravchenko.servlet.dto.OutGoingCommentDto;

public interface CommentMapper {

    OutGoingCommentDto map(Comment source);

    Comment map(IncomingCommentDto source);
}
