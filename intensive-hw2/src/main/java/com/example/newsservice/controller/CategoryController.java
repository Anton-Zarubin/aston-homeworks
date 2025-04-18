package com.example.newsservice.controller;

import com.example.newsservice.dto.category.CategoryListResponse;
import com.example.newsservice.dto.category.CategoryResponse;
import com.example.newsservice.dto.category.UpsertCategoryRequest;
import com.example.newsservice.service.CategoryService;
import com.example.newsservice.service.impl.CategoryServiceImpl;

public class CategoryController {

    private final CategoryService categoryService = CategoryServiceImpl.getInstance();

    private static CategoryController instance;

    private CategoryController() {}

    public static synchronized CategoryController getInstance() {
        if (instance == null) {
            instance = new CategoryController();
        }
        return instance;
    }

    public CategoryListResponse getAll() {
        return categoryService.getAll();
    }

    public CategoryResponse getById(Long id) {
        return categoryService.getById(id);
    }

    public CategoryResponse createCategory(UpsertCategoryRequest request) {
        return categoryService.create(request);
    }

    public boolean updateCategory(Long id, UpsertCategoryRequest request) {
        return categoryService.update(id, request);
    }

    public boolean deleteCategory(Long id) {
        return categoryService.deleteById(id);
    }
}
