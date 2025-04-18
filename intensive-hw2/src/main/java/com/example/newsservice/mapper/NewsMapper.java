package com.example.newsservice.mapper;

import com.example.newsservice.dto.news.NewsListResponse;
import com.example.newsservice.dto.news.NewsResponse;
import com.example.newsservice.dto.news.UpsertNewsRequest;
import com.example.newsservice.entity.News;
import com.example.newsservice.service.CategoryService;
import org.mapstruct.Context;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@DecoratedWith(NewsMapperDecorator.class)
@Mapper(uses = {CategoryMapper.class, TagMapper.class})
public interface NewsMapper {

    NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);

    News requestToNews(UpsertNewsRequest request, @Context CategoryService categoryService);

    @Mapping(source = "category.title", target = "category")
    NewsResponse newsToResponse(News news);

    List<NewsResponse> newsListToResponseList(List<News> news);

    default NewsListResponse newsListToNewsListResponse(List<News> news) {
        return new NewsListResponse(newsListToResponseList(news));
    }

}
