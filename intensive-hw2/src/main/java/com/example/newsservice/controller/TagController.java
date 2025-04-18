package com.example.newsservice.controller;

import com.example.newsservice.dto.tag.TagListResponse;
import com.example.newsservice.dto.tag.TagResponse;
import com.example.newsservice.dto.tag.UpsertTagRequest;
import com.example.newsservice.service.TagService;
import com.example.newsservice.service.impl.TagServiceImpl;

public class TagController {

    private final TagService tagService = TagServiceImpl.getInstance();

    private static TagController instance;

    private TagController() {}

    public static synchronized TagController getInstance() {
        if (instance == null) {
            instance = new TagController();
        }
        return instance;
    }

    public TagListResponse getAll() {
        return tagService.getAll();
    }

    public TagResponse getById(Long id) {
        return tagService.getById(id);
    }

    public TagResponse createTag(UpsertTagRequest request) {
        return tagService.create(request);
    }

    public boolean updateTag(Long id, UpsertTagRequest request) {
        return tagService.update(id, request);
    }

    public boolean deleteTag(Long id) {
        return tagService.deleteById(id);
    }
}
