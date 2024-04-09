package ru.kravchenko.servlet.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.kravchenko.model.News;

import java.util.List;

class TagDtoTest extends CommentDto {

    TagDto tagDto = new IncomingTagDto();

    @Test
    void getIdTest() {
        Assertions.assertNull(tagDto.getId());
    }

    @Test
    void setIdTest() {
        tagDto.setId(3L);
        Assertions.assertEquals(3L, tagDto.getId());
    }

    @Test
    void getNameTest() {
        Assertions.assertNull(tagDto.getName());
    }

    @Test
    void setNameTest() {
        tagDto.setName("name");
        Assertions.assertEquals("name", tagDto.getName());
    }

    @Test
    void getNewsListTest() {
        Assertions.assertNull(tagDto.getNewsList());
    }

    @Test
    void setNewsListTest() {
        List<News> newsList = List.of(new News(), new News());
        tagDto.setNewsList(newsList);
        Assertions.assertEquals(newsList, tagDto.getNewsList());
    }
}