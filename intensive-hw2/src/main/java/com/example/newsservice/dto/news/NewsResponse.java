package com.example.newsservice.dto.news;

import com.example.newsservice.dto.tag.TagResponse;

import java.util.Date;
import java.util.List;

public record NewsResponse(Long id, String title, String text, String category, List<TagResponse> tags, Date createdAt) {
}
