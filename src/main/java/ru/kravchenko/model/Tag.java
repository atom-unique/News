package ru.kravchenko.model;

import java.util.ArrayList;
import java.util.List;

public class Tag {

    private Long id;
    private String name;
    private List<News> newsList;

    public Tag() {
        this.newsList = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }
}
