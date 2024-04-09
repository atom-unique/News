package ru.kravchenko.repository.mapper;

import ru.kravchenko.model.Tag;

import java.sql.ResultSet;

public interface TagMapper {

    Tag map(ResultSet resultSet);
}
