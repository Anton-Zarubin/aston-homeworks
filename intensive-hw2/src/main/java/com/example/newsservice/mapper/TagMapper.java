package com.example.newsservice.mapper;

import com.example.newsservice.dto.tag.TagListResponse;
import com.example.newsservice.dto.tag.TagResponse;
import com.example.newsservice.dto.tag.UpsertTagRequest;
import com.example.newsservice.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    Tag requestToTag(UpsertTagRequest request);

    TagResponse tagToResponse(Tag tag);

    List<TagResponse> tagListToResponseList(List<Tag> tag);

    default TagListResponse tagListToTagListResponse(List<Tag> tags) {
        return new TagListResponse(tagListToResponseList(tags));
    }
}
