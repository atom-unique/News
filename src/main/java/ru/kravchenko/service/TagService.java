package ru.kravchenko.service;

import ru.kravchenko.model.Tag;

public interface TagService {

    Tag findById(Long id);

    Tag saveTag(Tag tag);

    void removeTag(Long id);
}
