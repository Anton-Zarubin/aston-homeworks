package com.example.newsservice.mapper;

import com.example.newsservice.dto.news.UpsertNewsRequest;
import com.example.newsservice.entity.News;
import com.example.newsservice.service.CategoryService;

public abstract class NewsMapperDecorator implements NewsMapper {

    @Override
    public News requestToNews(UpsertNewsRequest request, CategoryService categoryService) {
        News news = new News();
        news.setTitle(request.getTitle());
        news.setText(request.getText());
        news.setCategory(categoryService.getByTitle(request.getCategoryTitle()));

        return news;
    }
}
