package ru.kravchenko.repository.mapper;

import ru.kravchenko.model.News;

import java.sql.ResultSet;

public interface NewsMapper {

    News map(ResultSet resultSet);
}
