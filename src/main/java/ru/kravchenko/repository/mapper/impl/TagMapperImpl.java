package ru.kravchenko.repository.mapper.impl;

import ru.kravchenko.exception.ModelMappingException;
import ru.kravchenko.model.Tag;
import ru.kravchenko.repository.mapper.TagMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagMapperImpl implements TagMapper {

    private final Class<?> modelClass;

    public TagMapperImpl() {
        modelClass = Tag.class;
    }

    @Override
    public Tag map(ResultSet resultSet) {
        Tag tag = new Tag();
        try {
            tag.setId(resultSet.getLong("id"));
            tag.setName(resultSet.getString("name"));
        } catch (SQLException exception) {
            throw new ModelMappingException(modelClass);
        }
        return tag;
    }
}
