package com.example.newsservice.service;

import com.example.newsservice.dto.tag.TagListResponse;
import com.example.newsservice.dto.tag.TagResponse;
import com.example.newsservice.dto.tag.UpsertTagRequest;

public interface TagService {

    TagListResponse getAll();

    TagResponse getById(Long id);

    TagResponse create(UpsertTagRequest request);

    boolean update(long id, UpsertTagRequest request);

    boolean deleteById(Long id);
}
