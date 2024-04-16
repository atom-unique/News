package ru.kravchenko.repository;

import ru.kravchenko.model.Tag;

import java.util.Optional;

public interface TagRepository {

    Optional<Tag> findOne(Long id);

    Optional<Tag> create(Tag tag);

    void remove(Long id);
}
