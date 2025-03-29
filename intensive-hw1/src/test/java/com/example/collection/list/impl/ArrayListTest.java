package com.example.collection.list.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArrayListTest {

    private ArrayList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>();
    }

    @Test
    void testAdd() {
        int element1 = 5;
        int element2 = 10;
        list.add(element1);
        list.add(element2);
        assertEquals(2, list.size());
        assertEquals(element1, list.get(0));
        assertEquals(element2, list.get(1));
    }

    @Test
    void whenListIsFull_ThenElementIsAddedToList() {
        int initialCapacity = 10;
        list = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            list.add(i);
        }
        int newElement = 20;
        list.add(newElement);
        assertEquals(initialCapacity + 1, list.size());
        assertEquals(newElement, list.get(initialCapacity));
    }

    @Test
    void testAddByIndex_WhenListIsNotEmpty_ThenElementIsAdded() {
        int index = 0;
        int oldElement = 5;
        int newElement = 10;
        list.add(oldElement);
        assertEquals(oldElement, list.get(index));
        list.add(newElement, index);
        assertEquals(newElement, list.get(index));
    }

    @Test
    void testAddByIndex_WhenInvalidIndex_ThenException() {
        int invalidIndex = -1;
        int element = 5;
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(element, invalidIndex));
    }

    @Test
    void testSize() {
        int element1 = 5;
        int element2 = 10;
        list.add(element1);
        list.add(element2);
        assertEquals(2, list.size());
    }

    @Test
    void whenListIsNotEmptyAndIndexIsValid_ThenReturnElement() {
        int element = 5;
        list.add(element);
        assertEquals(element, list.get(0));
    }

    @Test
    void testGet_WhenIndexIsNegative_ThenException() {
        int negativeIndex = -1;
        list.add(5);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(negativeIndex));
    }

    @Test
    void testGet_WhenIndexIsGreaterThanSize_ThenException() {
        int index = 1;
        list.add(5);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(index));
    }

    @Test
    void whenListContainsSpecifiedElement_ThenElementIsRemoved() {
        int element = 5;
        list.add(element);
        list.remove(5);
        assertEquals(0, list.size());
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    }

    @Test
    void testClear() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.clear();
        assertEquals(0, list.size());
    }

    @Test
    void whenListIsNotEmpty_ThenListIsSortedInCorrectOrder() {
        list.add(3);
        list.add(1);
        list.add(2);
        list.sort();
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }
}
