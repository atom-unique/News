package ru.kravchenko.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class CommentTest {

    Comment comment = new Comment();

    @Test
    void getIdTest() {
        Assertions.assertNull(comment.getId());
    }

    @Test
    void setIdTest() {
        comment.setId(1L);
        Assertions.assertEquals(1L, comment.getId());
    }

    @Test
    void getNewsIdTest() {
        Assertions.assertNull(comment.getNewsId());
    }

    @Test
    void setNewsIdTest() {
        comment.setNewsId(2L);
        Assertions.assertEquals(2L, comment.getNewsId());
    }

    @Test
    void getAuthorTest() {
        Assertions.assertNull(comment.getAuthor());
    }

    @Test
    void setAuthorTest() {
        comment.setAuthor("author");
        Assertions.assertEquals("author", comment.getAuthor());
    }

    @Test
    void getDateTimeTest() {
        Assertions.assertNull(comment.getDateTime());
    }

    @Test
    void setDateTimeTest() {
        LocalDateTime dateTime = LocalDateTime.now();
        comment.setDateTime(dateTime);
        Assertions.assertEquals(dateTime, comment.getDateTime());
    }

    @Test
    void getTextTest() {
        Assertions.assertNull(comment.getText());
    }

    @Test
    void setTextTest() {
        comment.setText("text");
        Assertions.assertEquals("text", comment.getText());
    }
}