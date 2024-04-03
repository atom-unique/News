package ru.kravchenko.servlet.mapper.impl;

import ru.kravchenko.model.Comment;
import ru.kravchenko.servlet.dto.IncomingCommentDto;
import ru.kravchenko.servlet.dto.OutGoingCommentDto;
import ru.kravchenko.servlet.mapper.CommentMapper;

public class CommentMapperImpl implements CommentMapper {

    @Override
    public OutGoingCommentDto map(Comment source) {
        if (source == null) {
            return null;
        }
        OutGoingCommentDto target = new OutGoingCommentDto();
        target.setId(source.getId());
        target.setNewsId(source.getNewsId());
        target.setAuthor(source.getAuthor());
        target.setDateTime(source.getDateTime());
        target.setText(source.getText());
        return target;
    }

    @Override
    public Comment map(IncomingCommentDto source) {
        if (source == null) {
            return null;
        }
        Comment target = new Comment();
        target.setId(source.getId());
        target.setNewsId(source.getNewsId());
        target.setAuthor(source.getAuthor());
        target.setDateTime(source.getDateTime());
        target.setText(source.getText());
        return target;
    }
}
