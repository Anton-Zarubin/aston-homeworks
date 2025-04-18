package com.example.service;

import com.example.newsservice.dto.category.CategoryResponse;
import com.example.newsservice.dto.category.UpsertCategoryRequest;
import com.example.newsservice.entity.Category;
import com.example.newsservice.repository.CrudRepository;
import com.example.newsservice.repository.impl.CategoryRepository;
import com.example.newsservice.service.CategoryService;
import com.example.newsservice.service.impl.CategoryServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    private static CategoryService categoryService;

    private static CrudRepository<Long, Category> repository;

    private static CategoryRepository oldInstance;

    private final String categoryTitle = "some category";

    private Category category;

    private List<Category> categories;

    private static void setMock(CrudRepository<Long, Category> mock) {
        try {
            Field instance = CategoryRepository.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldInstance = (CategoryRepository) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        repository = mock(CategoryRepository.class);
        setMock(repository);
        categoryService = CategoryServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = CategoryRepository.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldInstance);
    }

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setTitle(categoryTitle);

        categories = Collections.singletonList(category);
    }

    @Test
    void getAll() {
        when(repository.findAll()).thenReturn(categories);
        assertDoesNotThrow(() -> categoryService.getAll());
    }

    @Test
    void whenExists_thanReturnCategory() {
        when(repository.findById(1L)).thenReturn(Optional.of(category));
        CategoryResponse resultCategory = categoryService.getById(1L);
        assertNotNull(resultCategory);
        assertEquals(categoryTitle, resultCategory.title());
    }

    @Test
    void whenCategoryNotFound_thanException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> categoryService.getById(1L));
    }

    @Test
    public void createCategory() {
        assertDoesNotThrow(() -> categoryService.create(new UpsertCategoryRequest(categoryTitle)));
        verify(repository).save(any(Category.class));
    }

    @Test
    public void updateCategory() {
        assertDoesNotThrow(() -> categoryService.update(1L, new UpsertCategoryRequest(categoryTitle)));
        verify(repository).update(anyLong(), any(Category.class));
    }

    @Test
    void testDelete() {
        categoryService.deleteById(1L);
        verify(repository).deleteById(1L);
    }
}
