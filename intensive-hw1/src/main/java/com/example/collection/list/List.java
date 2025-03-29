package com.example.collection.list;

/**
 * The List interface represents a contract for a list data structure.
 *
 * @param <T> the type of elements in the list
 */
public interface List<T> {

    void add(T value);

    void add(T value, int index);

    int size();

    T get(int index);

    void remove(T value);

    void clear();

    void sort();
}
