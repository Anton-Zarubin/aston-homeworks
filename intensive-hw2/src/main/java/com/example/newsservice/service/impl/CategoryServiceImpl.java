package com.example.newsservice.service.impl;

import com.example.newsservice.dto.category.CategoryListResponse;
import com.example.newsservice.dto.category.CategoryResponse;
import com.example.newsservice.dto.category.UpsertCategoryRequest;
import com.example.newsservice.entity.Category;
import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.mapper.CategoryMapper;
import com.example.newsservice.repository.CrudRepository;
import com.example.newsservice.repository.impl.CategoryRepository;
import com.example.newsservice.service.CategoryService;

public class CategoryServiceImpl implements CategoryService {

    private final CrudRepository<Long, Category> repository = CategoryRepository.getInstance();

    private final CategoryMapper mapper = CategoryMapper.INSTANCE;

    private static CategoryService instance;

    private CategoryServiceImpl() {
    }

    public static synchronized CategoryService getInstance() {
        if (instance == null) {
            instance = new CategoryServiceImpl();
        }
        return instance;
    }

    @Override
    public CategoryListResponse getAll() {
        return mapper.categoryListToCategoryListResponse(repository.findAll());
    }

    @Override
    public CategoryResponse getById(Long id) {
        return mapper.categoryToResponse(repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("categoryById", id)));
    }

    @Override
    public Category getByTitle(String title) {
        return repository.findByName(title).orElseThrow(() ->
                new EntityNotFoundException("categoryByTitle", title));
    }

    @Override
    public CategoryResponse create(UpsertCategoryRequest request) {
        return mapper.categoryToResponse(repository.save(mapper.requestToCategory(request)));
    }

    @Override
    public boolean update(long id, UpsertCategoryRequest request) {
        return repository.update(id, mapper.requestToCategory(request));
    }

    @Override
    public boolean deleteById(Long id) {
        return repository.deleteById(id);
    }
}
