package com.example.newsservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class Category {

    private long id;

    private String title;

    private List<News> news;

    public Category(long id, String title) {
        this.id = id;
        this.title = title;
    }
}