package com.example.newsservice.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<K, T> {

    List<T> findAll();

    Optional<T> findById(K k);

    Optional<T> findByName(String name);

    T save(T t);

    boolean update(K k, T t);

    boolean deleteById(K k);

    boolean existsById(K k);
}