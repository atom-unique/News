package ru.kravchenko.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    Tag tag = new Tag();

    @Test
    void getIdTest() {
        Assertions.assertNull(tag.getId());
    }

    @Test
    void setIdTest() {
        tag.setId(3L);
        Assertions.assertEquals(3L, tag.getId());
    }

    @Test
    void getNameTest() {
        Assertions.assertNull(tag.getName());
    }

    @Test
    void setNameTest() {
        tag.setName("name");
        Assertions.assertEquals("name", tag.getName());
    }

    @Test
    void getNewsListTest() {
        Assertions.assertEquals(new ArrayList<News>(), tag.getNewsList());
    }

    @Test
    void setNewsListTest() {
        List<News> newsList = List.of(new News(), new News());
        tag.setNewsList(newsList);
        Assertions.assertEquals(newsList, tag.getNewsList());
    }
}