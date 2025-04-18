package com.example.newsservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class Tag {

    private long id;

    private String name;

    private List<News> news;
}
