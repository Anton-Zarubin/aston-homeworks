package com.example.newsservice.repository;

import com.example.newsservice.entity.News;

public interface NewsRepository extends CrudRepository<Long, News> {

    void addTag(Long newsId, Long tagId);

    boolean removeTag(Long newsId, Long tagId);
}
