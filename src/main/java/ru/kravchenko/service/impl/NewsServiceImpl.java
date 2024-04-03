package ru.kravchenko.service.impl;

import ru.kravchenko.exception.EntityCreateException;
import ru.kravchenko.exception.EntityNotFoundException;
import ru.kravchenko.exception.EntityUpdateException;
import ru.kravchenko.model.News;
import ru.kravchenko.repository.NewsRepository;
import ru.kravchenko.repository.impl.NewsRepositoryImpl;
import ru.kravchenko.service.NewsService;

import java.sql.Connection;
import java.util.List;

public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final Class<?> modelClass;

    public NewsServiceImpl() {
        this.newsRepository = new NewsRepositoryImpl();
        this.modelClass = News.class;
    }

    public NewsServiceImpl(Connection connection) {
        this.newsRepository = new NewsRepositoryImpl(connection);
        this.modelClass = News.class;
    }

    @Override
    public News findById(Long id) {
        return newsRepository.findOne(id).orElseThrow(
                () -> new EntityNotFoundException(modelClass, id)
        );
    }

    @Override
    public List<News> findAllNews() {
        List<News> newsList = newsRepository.findAll();
        return newsList.isEmpty() ? List.of() : newsList;
    }

    @Override
    public News saveNews(News news) {
        if (news.getId() != null && newsRepository.findOne(news.getId()).isPresent()) {
            return newsRepository.update(news).orElseThrow(
                    () -> new EntityUpdateException(modelClass)
            );
        } else {
            return newsRepository.create(news).orElseThrow(
                    () -> new EntityCreateException(modelClass)
            );
        }
    }

    @Override
    public void removeNews(Long id) {
        newsRepository.remove(id);
    }
}
