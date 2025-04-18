package com.example.service;

import com.example.newsservice.dto.news.NewsResponse;
import com.example.newsservice.dto.news.UpsertNewsRequest;
import com.example.newsservice.entity.Category;
import com.example.newsservice.entity.News;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.repository.impl.NewsRepositoryImpl;
import com.example.newsservice.service.CategoryService;
import com.example.newsservice.service.NewsService;
import com.example.newsservice.service.impl.CategoryServiceImpl;
import com.example.newsservice.service.impl.NewsServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {

    private static NewsService newsService;

    private static NewsRepository repository;

    private static NewsRepositoryImpl oldNewsRepositoryInstance;

    private static CategoryService categoryService;

    private static CategoryServiceImpl oldCategoryServiceInstance;

    private final String categoryTitle = "any category";

    private final String newsTitle = "some news";

    private Category category;

    private News news;

    private List<News> newslist;

    private UpsertNewsRequest request;

    private static void setMock(NewsRepository mock) {
        try {
            Field instance = NewsRepositoryImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldNewsRepositoryInstance = (NewsRepositoryImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setMock(CategoryService mock) {
        try {
            Field instance = CategoryServiceImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldCategoryServiceInstance = (CategoryServiceImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        repository = mock(NewsRepository.class);
        setMock(repository);
        categoryService = mock(CategoryService.class);
        setMock(categoryService);
        newsService = NewsServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = NewsRepositoryImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldNewsRepositoryInstance);

        instance = CategoryServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldCategoryServiceInstance);
    }

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setTitle(categoryTitle);

        news = new News();
        news.setTitle(newsTitle);
        news.setText("some text");
        news.setCategory(category);

        newslist = Collections.singletonList(news);

        request = new UpsertNewsRequest();
        request.setTitle(newsTitle);
        request.setText("some text");
        request.setCategoryTitle(categoryTitle);
    }

    @Test
    void getAll() {
        when(repository.findAll()).thenReturn(newslist);
        assertDoesNotThrow(() -> newsService.getAll());
    }

    @Test
    void whenExists_thanReturnNews() {
        when(repository.findById(1L)).thenReturn(Optional.of(news));
        NewsResponse resultTag = newsService.getById(1L);
        assertNotNull(resultTag);
        assertEquals(newsTitle, resultTag.title());
    }

    @Test
    void whenNewsNotFound_thanException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> newsService.getById(1L));
    }

    @Test
    public void createNews() {
        when(categoryService.getByTitle(categoryTitle)).thenReturn(category);
        assertDoesNotThrow(() -> newsService.create(request));
        verify(repository).save(any(News.class));
    }

    @Test
    public void updateNews() {
        when(categoryService.getByTitle(categoryTitle)).thenReturn(category);
        assertDoesNotThrow(() -> newsService.update(1L, request));
        verify(repository).update(anyLong(), any(News.class));
    }

    @Test
    void testDelete() {
        newsService.deleteById(1L);
        verify(repository).deleteById(1L);
    }
}
