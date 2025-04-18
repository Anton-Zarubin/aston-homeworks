package com.example.newsservice.controller;

import com.example.newsservice.dto.news.NewsListResponse;
import com.example.newsservice.dto.news.NewsResponse;
import com.example.newsservice.dto.news.UpsertNewsRequest;
import com.example.newsservice.service.NewsService;
import com.example.newsservice.service.impl.NewsServiceImpl;

public class NewsController {

    private final NewsService newsService = NewsServiceImpl.getInstance();

    private static NewsController instance;

    private NewsController() {}

    public static synchronized NewsController getInstance() {
        if (instance == null) {
            instance = new NewsController();
        }
        return instance;
    }

    public NewsListResponse getAll() {
        return newsService.getAll();
    }

    public NewsResponse getById(Long id) {
        return newsService.getById(id);
    }

    public NewsResponse createNews(UpsertNewsRequest request) {
        return newsService.create(request);
    }

    public boolean updateNews(Long id, UpsertNewsRequest request) {
        return newsService.update(id, request);
    }

    public void addTag(Long newsId, Long tagId) {
        newsService.addTag(newsId, tagId);
    }

    public boolean removeTag(Long newsId, Long tagId) {
        return newsService.removeTag(newsId, tagId);
    }

    public boolean deleteNews(Long id) {
        return newsService.deleteById(id);
    }
}
