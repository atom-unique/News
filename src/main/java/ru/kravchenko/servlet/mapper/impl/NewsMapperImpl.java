package ru.kravchenko.servlet.mapper.impl;

import ru.kravchenko.model.News;
import ru.kravchenko.servlet.dto.IncomingNewsDto;
import ru.kravchenko.servlet.dto.OutGoingNewsDto;
import ru.kravchenko.servlet.mapper.NewsMapper;

public class NewsMapperImpl implements NewsMapper {

    @Override
    public OutGoingNewsDto map(News source) {
        if (source == null) {
            return null;
        }
        OutGoingNewsDto target = new OutGoingNewsDto();
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setAuthor(source.getAuthor());
        target.setDateTime(source.getDateTime());
        target.setText(source.getText());
        target.setCommentList(source.getCommentList());
        target.setTagList(source.getTagList());
        return target;
    }

    @Override
    public News map(IncomingNewsDto source) {
        if (source == null) {
            return null;
        }
        News target = new News();
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setAuthor(source.getAuthor());
        target.setDateTime(source.getDateTime());
        target.setText(source.getText());
        target.setCommentList(source.getCommentList());
        target.setTagList(source.getTagList());
        return target;
    }
}
