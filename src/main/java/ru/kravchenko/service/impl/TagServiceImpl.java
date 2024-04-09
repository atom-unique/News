package ru.kravchenko.service.impl;

import ru.kravchenko.exception.EntityCreateException;
import ru.kravchenko.exception.EntityNotFoundException;
import ru.kravchenko.model.Tag;
import ru.kravchenko.repository.TagRepository;
import ru.kravchenko.repository.impl.TagRepositoryImpl;
import ru.kravchenko.service.TagService;

import java.sql.Connection;
import java.util.List;

public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final Class<?> modelClass;

    public TagServiceImpl() {
        this.tagRepository = new TagRepositoryImpl();
        this.modelClass = Tag.class;
    }

    public TagServiceImpl(Connection connection) {
        this.tagRepository = new TagRepositoryImpl(connection);
        this.modelClass = Tag.class;
    }

    @Override
    public Tag findById(Long id) {
        return tagRepository.findOne(id).orElseThrow(
                () -> new EntityNotFoundException(modelClass, id)
        );
    }

    @Override
    public List<Tag> findAllTags(Long id) {
        List<Tag> tagList = tagRepository.findAll(id);
        return tagList.isEmpty() ? List.of() : tagList;
    }

    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.create(tag).orElseThrow(
                () -> new EntityCreateException(modelClass)
        );
    }

    @Override
    public void removeTag(Long id) {
        tagRepository.remove(id);
    }
}
