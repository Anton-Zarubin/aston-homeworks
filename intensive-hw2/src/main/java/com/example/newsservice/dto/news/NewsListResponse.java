package com.example.newsservice.dto.news;

import java.util.List;

public record NewsListResponse(List<NewsResponse> news) {
}
