package com.example.newsservice.service;

import com.example.newsservice.dto.category.CategoryListResponse;
import com.example.newsservice.dto.category.CategoryResponse;
import com.example.newsservice.dto.category.UpsertCategoryRequest;
import com.example.newsservice.entity.Category;

public interface CategoryService {

    CategoryListResponse getAll();

    CategoryResponse getById(Long id);

    Category getByTitle(String title);

    CategoryResponse create(UpsertCategoryRequest request);

    boolean update(long id, UpsertCategoryRequest request);

    boolean deleteById(Long id);
}
