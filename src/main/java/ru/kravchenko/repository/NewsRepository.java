package ru.kravchenko.repository;

import ru.kravchenko.model.News;

import java.util.List;

public interface NewsRepository extends CommonRepository<News> {

    List<News> findAll();
}
