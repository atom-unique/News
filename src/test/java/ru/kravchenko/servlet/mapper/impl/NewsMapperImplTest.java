package ru.kravchenko.servlet.mapper.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.kravchenko.model.News;
import ru.kravchenko.servlet.dto.IncomingNewsDto;
import ru.kravchenko.servlet.dto.OutGoingNewsDto;
import ru.kravchenko.servlet.mapper.NewsMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;

class NewsMapperImplTest {

    @Mock
    private NewsMapper newsMapper = new NewsMapperImpl();

    @Test
    void mapTestModelToDto() {
        News news = new News();
        news.setTitle("Заголовок");
        news.setAuthor("Автор");
        LocalDateTime dateTime = LocalDateTime.now();
        news.setDateTime(dateTime);
        news.setText("Текст");
        news.setCommentList(new ArrayList<>());
        news.setTagList(new ArrayList<>());
        OutGoingNewsDto dto = newsMapper.map(news);
        Assertions.assertEquals("Заголовок", dto.getTitle());
        Assertions.assertEquals("Автор", dto.getAuthor());
        Assertions.assertEquals(dateTime, dto.getDateTime());
        Assertions.assertEquals("Текст", dto.getText());
    }

    @Test
    void mapTestDtoToModel() {
        IncomingNewsDto dto = new IncomingNewsDto();
        dto.setTitle("Заголовок");
        dto.setAuthor("Автор");
        LocalDateTime dateTime = LocalDateTime.now();
        dto.setDateTime(dateTime);
        dto.setText("Текст");
        dto.setCommentList(new ArrayList<>());
        dto.setTagList(new ArrayList<>());
        News news = newsMapper.map(dto);
        Assertions.assertEquals("Заголовок", news.getTitle());
        Assertions.assertEquals("Автор", news.getAuthor());
        Assertions.assertEquals(dateTime, news.getDateTime());
        Assertions.assertEquals("Текст", news.getText());
    }
}