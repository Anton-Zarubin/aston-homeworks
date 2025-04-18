package com.example.newsservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class News {

    private long id;

    private String title;

    private String text;

    private Category category;

    private List<Tag> tags;

    private Date createdAt;
}
