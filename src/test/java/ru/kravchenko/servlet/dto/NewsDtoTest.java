package ru.kravchenko.servlet.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.kravchenko.model.Comment;
import ru.kravchenko.model.Tag;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

class NewsDtoTest extends CommentDto {

    NewsDto newsDto = new OutGoingNewsDto();

    @Test
    void getIdTest() {
        assertNull(newsDto.getId());
    }

    @Test
    void setIdTest() {
        newsDto.setId(1L);
        Assertions.assertEquals(1L, newsDto.getId());
    }

    @Test
    void getTitleTest() {
        Assertions.assertNull(newsDto.getTitle());
    }

    @Test
    void setTitleTest() {
        newsDto.setTitle("title");
        Assertions.assertEquals("title", newsDto.getTitle());
    }

    @Test
    void getAuthorTest() {
        Assertions.assertNull(newsDto.getAuthor());
    }

    @Test
    void setAuthorTest() {
        newsDto.setAuthor("author");
        Assertions.assertEquals("author", newsDto.getAuthor());
    }

    @Test
    void getDateTimeTest() {
        Assertions.assertNull(newsDto.getDateTime());
    }

    @Test
    void setDateTimeTest() {
        LocalDateTime dateTime = LocalDateTime.now();
        newsDto.setDateTime(dateTime);
        Assertions.assertEquals(dateTime, newsDto.getDateTime());
    }

    @Test
    void getTextTest() {
        Assertions.assertNull(newsDto.getText());
    }

    @Test
    void setTextTest() {
        newsDto.setText("text");
        Assertions.assertEquals("text", newsDto.getText());
    }

    @Test
    void getCommentListTest() {
        Assertions.assertNull(newsDto.getCommentList());
    }

    @Test
    void setCommentListTest() {
        List<Comment> commentList = List.of(new Comment(), new Comment());
        newsDto.setCommentList(commentList);
        Assertions.assertEquals(commentList, newsDto.getCommentList());
    }

    @Test
    void getTagListTest() {
        Assertions.assertNull(newsDto.getTagList());
    }

    @Test
    void setTagListTest() {
        List<Tag> tagList = List.of(new Tag(), new Tag());
        newsDto.setTagList(tagList);
        Assertions.assertEquals(tagList, newsDto.getTagList());
    }
}