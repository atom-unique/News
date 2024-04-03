package ru.kravchenko.servlet.mapper;

import ru.kravchenko.model.News;
import ru.kravchenko.servlet.dto.IncomingNewsDto;
import ru.kravchenko.servlet.dto.OutGoingNewsDto;

public interface NewsMapper {

    OutGoingNewsDto map(News source);

    News map(IncomingNewsDto source);
}
