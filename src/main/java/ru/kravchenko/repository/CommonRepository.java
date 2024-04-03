package ru.kravchenko.repository;

import java.util.Optional;

public interface CommonRepository<M> {

    Optional<M> findOne(Long id);

    Optional<M> create(M model);

    Optional<M> update(M model);

    void remove(Long id);
}
