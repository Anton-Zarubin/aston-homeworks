package com.example.service;

import com.example.newsservice.dto.tag.TagResponse;
import com.example.newsservice.dto.tag.UpsertTagRequest;
import com.example.newsservice.entity.Tag;
import com.example.newsservice.repository.CrudRepository;
import com.example.newsservice.repository.impl.TagRepository;
import com.example.newsservice.service.TagService;
import com.example.newsservice.service.impl.TagServiceImpl;
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
public class TagServiceTest {

    private static TagService tagService;

    private static CrudRepository<Long, Tag> repository;

    private static TagRepository oldInstance;

    private final String tagName = "some tag";

    private Tag tag;

    private List<Tag> tags;

    private static void setMock(CrudRepository<Long, Tag> mock) {
        try {
            Field instance = TagRepository.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldInstance = (TagRepository) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        repository = mock(TagRepository.class);
        setMock(repository);
        tagService = TagServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = TagRepository.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldInstance);
    }

    @BeforeEach
    public void setUp() {
        tag = new Tag();
        tag.setName(tagName);

        tags = Collections.singletonList(tag);
    }

    @Test
    void getAll() {
        when(repository.findAll()).thenReturn(tags);
        assertDoesNotThrow(() -> tagService.getAll());
    }

    @Test
    void whenExists_thanReturnTag() {
        when(repository.findById(1L)).thenReturn(Optional.of(tag));
        TagResponse resultTag = tagService.getById(1L);
        assertNotNull(resultTag);
        assertEquals(tagName, resultTag.name());
    }

    @Test
    void whenTagNotFound_thanException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> tagService.getById(1L));
    }

    @Test
    public void createTag() {
        assertDoesNotThrow(() -> tagService.create(new UpsertTagRequest(tagName)));
        verify(repository).save(any(Tag.class));
    }

    @Test
    public void updateTag() {
        assertDoesNotThrow(() -> tagService.update(1L, new UpsertTagRequest(tagName)));
        verify(repository).update(anyLong(), any(Tag.class));
    }

    @Test
    void testDelete() {
        tagService.deleteById(1L);
        verify(repository).deleteById(1L);
    }
}
