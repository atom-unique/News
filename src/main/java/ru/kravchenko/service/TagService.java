package ru.kravchenko.service;

import ru.kravchenko.model.Tag;

import java.util.List;

public interface TagService {

    Tag findById(Long id);

    List<Tag> findAllTags(Long id);

    Tag saveTag(Tag tag);

    void removeTag(Long id);
}
