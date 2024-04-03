package ru.kravchenko.servlet.mapper.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.kravchenko.model.Comment;
import ru.kravchenko.servlet.dto.IncomingCommentDto;
import ru.kravchenko.servlet.dto.OutGoingCommentDto;
import ru.kravchenko.servlet.mapper.CommentMapper;

import java.time.LocalDateTime;

class CommentMapperImplTest {

    @Mock
    private CommentMapper commentMapper = new CommentMapperImpl();

    @Test
    void mapTestModelToDto() {
        Comment comment = new Comment();
        comment.setNewsId(1L);
        comment.setAuthor("автор");
        LocalDateTime dateTime = LocalDateTime.now();
        comment.setDateTime(dateTime);
        comment.setText("текст");
        OutGoingCommentDto dto = commentMapper.map(comment);
        Assertions.assertEquals(1, dto.getNewsId());
        Assertions.assertEquals("автор", dto.getAuthor());
        Assertions.assertEquals(dateTime, dto.getDateTime());
        Assertions.assertEquals("текст", dto.getText());
    }

    @Test
    void mapTestDtoToModel() {
        IncomingCommentDto dto = new IncomingCommentDto();
        dto.setNewsId(1L);
        dto.setAuthor("автор");
        LocalDateTime dateTime = LocalDateTime.now();
        dto.setDateTime(dateTime);
        dto.setText("текст");
        Comment comment = commentMapper.map(dto);
        Assertions.assertEquals(1, comment.getNewsId());
        Assertions.assertEquals("автор", comment.getAuthor());
        Assertions.assertEquals(dateTime, comment.getDateTime());
        Assertions.assertEquals("текст", comment.getText());
    }
}