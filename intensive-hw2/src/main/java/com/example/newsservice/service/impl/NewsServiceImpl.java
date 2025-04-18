package com.example.newsservice.service.impl;

import com.example.newsservice.dto.news.NewsListResponse;
import com.example.newsservice.dto.news.NewsResponse;
import com.example.newsservice.dto.news.UpsertNewsRequest;
import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.mapper.NewsMapper;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.repository.impl.NewsRepositoryImpl;
import com.example.newsservice.service.CategoryService;
import com.example.newsservice.service.NewsService;

public class NewsServiceImpl implements NewsService {

    private final NewsRepository repository = NewsRepositoryImpl.getInstance();

    private final CategoryService categoryService = CategoryServiceImpl.getInstance();

    private final NewsMapper mapper = NewsMapper.INSTANCE;

    private static NewsService instance;

    private NewsServiceImpl() {}

    public static synchronized NewsService getInstance() {
        if (instance == null) {
            instance = new NewsServiceImpl();
        }
        return instance;
    }

    @Override
    public NewsListResponse getAll() {
        return mapper.newsListToNewsListResponse(repository.findAll());
    }

    @Override
    public NewsResponse getById(Long id) {
        return mapper.newsToResponse(repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("newsById", id)));
    }

    @Override
    public NewsResponse create(UpsertNewsRequest request) {
        return mapper.newsToResponse(repository.save(mapper.requestToNews(request, categoryService)));
    }

    @Override
    public boolean update(long id, UpsertNewsRequest request) {
        return repository.update( id, mapper.requestToNews(request, categoryService));
    }

    @Override
    public void addTag(Long newsId, Long tagId) {
        repository.addTag(newsId, tagId);
    }

    @Override
    public boolean removeTag(Long newsId,Long tagId) {
        return repository.removeTag(newsId, tagId);
    }

    @Override
    public boolean deleteById(Long id) {
        return repository.deleteById(id);
    }
}
