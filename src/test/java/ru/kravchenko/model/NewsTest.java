package ru.kravchenko.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

class NewsTest {

    News news = new News();

    @Test
    void getIdTest() {
        assertNull(news.getId());
    }

    @Test
    void setIdTest() {
        news.setId(1L);
        Assertions.assertEquals(1L, news.getId());
    }

    @Test
    void getTitleTest() {
        Assertions.assertNull(news.getTitle());
    }

    @Test
    void setTitleTest() {
        news.setTitle("title");
        Assertions.assertEquals("title", news.getTitle());
    }

    @Test
    void getAuthorTest() {
        Assertions.assertNull(news.getAuthor());
    }

    @Test
    void setAuthorTest() {
        news.setAuthor("author");
        Assertions.assertEquals("author", news.getAuthor());
    }

    @Test
    void getDateTimeTest() {
        Assertions.assertNull(news.getDateTime());
    }

    @Test
    void setDateTimeTest() {
        LocalDateTime dateTime = LocalDateTime.now();
        news.setDateTime(dateTime);
        Assertions.assertEquals(dateTime, news.getDateTime());
    }

    @Test
    void getTextTest() {
        Assertions.assertNull(news.getText());
    }

    @Test
    void setTextTest() {
        news.setText("text");
        Assertions.assertEquals("text", news.getText());
    }

    @Test
    void getCommentListTest() {
        Assertions.assertEquals(new ArrayList<Comment>(), news.getCommentList());
    }

    @Test
    void setCommentListTest() {
        List<Comment> commentList = List.of(new Comment(), new Comment());
        news.setCommentList(commentList);
        Assertions.assertEquals(commentList, news.getCommentList());
    }

    @Test
    void getTagListTest() {
        Assertions.assertEquals(new ArrayList<Tag>(), news.getTagList());
    }

    @Test
    void setTagListTest() {
        List<Tag> tagList = List.of(new Tag(), new Tag());
        news.setTagList(tagList);
        Assertions.assertEquals(tagList, news.getTagList());
    }
}