package ru.kravchenko.repository.mapper.impl;

import ru.kravchenko.exception.ModelMappingException;
import ru.kravchenko.model.Tag;
import ru.kravchenko.repository.NewsRepository;
import ru.kravchenko.repository.impl.NewsRepositoryImpl;
import ru.kravchenko.repository.mapper.TagMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TagMapperImpl implements TagMapper {


    private final NewsRepository newsRepository;
    private final Class<?> modelClass;

    public TagMapperImpl() {
        this.newsRepository = new NewsRepositoryImpl();
        modelClass = Tag.class;
    }

    public TagMapperImpl(Connection connection) {
        this.newsRepository = new NewsRepositoryImpl(connection);
        this.modelClass = Tag.class;
    }

    @Override
    public Tag map(ResultSet resultSet) {
        Tag tag = new Tag();
        try {
            tag.setId(resultSet.getLong("id"));
            tag.setName(resultSet.getString("name"));
            tag.setNewsList(newsRepository.findAllByTagId(tag.getId()));
        } catch (SQLException exception) {
            throw new ModelMappingException(modelClass);
        }
        return tag;
    }
}
