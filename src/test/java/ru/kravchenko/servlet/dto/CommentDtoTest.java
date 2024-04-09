package ru.kravchenko.servlet.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentDtoTest extends CommentDto {

    CommentDto commentDto = new IncomingCommentDto();

    @Test
    void getIdTest() {
        assertNull(commentDto.getId());
    }

    @Test
    void setIdTest() {
        commentDto.setId(1L);
        Assertions.assertEquals(1L, commentDto.getId());
    }

    @Test
    void getNewsIdTest() {
        Assertions.assertNull(commentDto.getNewsId());
    }

    @Test
    void setNewsIdTest() {
        commentDto.setNewsId(2L);
        Assertions.assertEquals(2L, commentDto.getNewsId());
    }

    @Test
    void getAuthorTest() {
        Assertions.assertNull(commentDto.getAuthor());
    }

    @Test
    void setAuthorTest() {
        commentDto.setAuthor("author");
        Assertions.assertEquals("author", commentDto.getAuthor());
    }

    @Test
    void getDateTimeTest() {
        Assertions.assertNull(commentDto.getDateTime());
    }

    @Test
    void setDateTimeTest() {
        LocalDateTime dateTime = LocalDateTime.now();
        commentDto.setDateTime(dateTime);
        Assertions.assertEquals(dateTime, commentDto.getDateTime());
    }

    @Test
    void getTextTest() {
        Assertions.assertNull(commentDto.getText());
    }

    @Test
    void setTextTest() {
        commentDto.setText("text");
        Assertions.assertEquals("text", commentDto.getText());
    }
}