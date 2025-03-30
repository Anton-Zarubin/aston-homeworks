package com.example.collection.list.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LinkedListTest {

    private LinkedList<Integer> linkedList;

    @BeforeEach
    void setUp() {
        linkedList = new LinkedList<>();
    }

    @Test
    void testAdd() {
        int element1 = 5;
        int element2 = 10;
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(2, linkedList.size());
        assertEquals(element1, linkedList.get(0));
        assertEquals(element2, linkedList.get(1));
    }

    @Test
    void testAddByIndex() {
        int element1 = 5;
        int element2 = 10;
        int element3 = 15;
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3, 1);
        assertEquals(3, linkedList.size());
        assertEquals(element1, linkedList.get(0));
        assertEquals(element3, linkedList.get(1));
        assertEquals(element2, linkedList.get(2));
    }

    @Test
    void testAddByIndex_WhenInvalidIndex_ThenException() {
        int element = 5;
        assertThrows(IndexOutOfBoundsException.class, () -> linkedList.add(element, 1));
    }

    @Test
    void whenListIsNotEmptyAndIndexIsValid_ThenReturnElement() {
        int element1 = 5;
        int element2 = 10;
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(element1, linkedList.get(0));
        assertEquals(element2, linkedList.get(1));
    }

    @Test
    void testGet_WhenIndexIsNegative_ThenException() {
        int element = 5;
        linkedList.add(element);
        assertThrows(IndexOutOfBoundsException.class, () -> linkedList.get(-1));
    }

    @Test
    void testGet_WhenIndexIsGreaterThanSize_ThenException() {
        int element = 5;
        linkedList.add(element);
        assertThrows(IndexOutOfBoundsException.class, () -> linkedList.get(2));
    }

    @Test
    void whenListContainsSpecifiedElement_ThenElementIsRemoved() {
        linkedList.add(1);
        linkedList.add(2);
        linkedList.add(3);
        linkedList.remove(2);
        assertEquals(2, linkedList.size());
        assertEquals(1, linkedList.get(0));
        assertEquals(3, linkedList.get(1));
    }

    @Test
    void testClear() {
        linkedList.add(1);
        linkedList.add(2);
        linkedList.add(3);
        linkedList.clear();
        assertEquals(0, linkedList.size());
    }

    @Test
    void whenListIsNotEmpty_ThenListIsSortedInCorrectOrder() {
        linkedList.add(3);
        linkedList.add(1);
        linkedList.add(2);
        linkedList.sort();
        assertEquals(3, linkedList.size());
        assertEquals(1, linkedList.get(0));
        assertEquals(2, linkedList.get(1));
        assertEquals(3, linkedList.get(2));
    }
}
