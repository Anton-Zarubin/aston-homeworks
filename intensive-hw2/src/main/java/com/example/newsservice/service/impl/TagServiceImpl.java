package com.example.newsservice.service.impl;

import com.example.newsservice.dto.tag.TagListResponse;
import com.example.newsservice.dto.tag.TagResponse;
import com.example.newsservice.dto.tag.UpsertTagRequest;
import com.example.newsservice.entity.Tag;
import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.mapper.TagMapper;
import com.example.newsservice.repository.CrudRepository;
import com.example.newsservice.repository.impl.TagRepository;
import com.example.newsservice.service.TagService;

public class TagServiceImpl implements TagService {

    private final CrudRepository<Long, Tag> repository = TagRepository.getInstance();

    private final TagMapper mapper = TagMapper.INSTANCE;

    private static TagService instance;

    private TagServiceImpl() {
    }

    public static synchronized TagService getInstance() {
        if (instance == null) {
            instance = new TagServiceImpl();
        }
        return instance;
    }

    @Override
    public TagListResponse getAll() {
        return mapper.tagListToTagListResponse(repository.findAll());
    }

    @Override
    public TagResponse getById(Long id) {
        return mapper.tagToResponse(repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("tagById", id)));
    }

    @Override
    public TagResponse create(UpsertTagRequest request) {
        return mapper.tagToResponse(repository.save(mapper.requestToTag(request)));
    }

    @Override
    public boolean update(long id, UpsertTagRequest request) {
        return repository.update(id, mapper.requestToTag(request));
    }

    @Override
    public boolean deleteById(Long id) {
        return repository.deleteById(id);
    }
}
