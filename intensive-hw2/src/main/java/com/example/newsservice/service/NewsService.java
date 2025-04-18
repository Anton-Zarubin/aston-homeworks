package com.example.newsservice.service;

import com.example.newsservice.dto.news.NewsListResponse;
import com.example.newsservice.dto.news.NewsResponse;
import com.example.newsservice.dto.news.UpsertNewsRequest;

public interface NewsService {

    NewsListResponse getAll();

    NewsResponse getById(Long id);

    NewsResponse create(UpsertNewsRequest request);

    boolean update(long id, UpsertNewsRequest request);

    void addTag(Long newsId, Long tagId);

    boolean removeTag(Long newsId, Long tagId);

    boolean deleteById(Long id);
}
