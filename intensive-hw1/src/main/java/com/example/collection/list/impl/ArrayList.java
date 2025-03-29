package com.example.collection.list.impl;

import com.example.collection.list.List;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

/**
 * Implementation of the List using an ArrayList-based approach.
 *
 * @param <T> the type of elements in the list
 */
public class ArrayList<T> implements List<T> {

    /**
     * Default array capacity.
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * An array to store elements of type T.
     */
    private T[] values;

    /**
     * The current size of the array, representing the number of elements it contains.
     */
    private int size;

    /**
     * Constructs an ArrayList with the default capacity.
     */
    @SuppressWarnings("unchecked")
    public ArrayList() {
        values = (T[]) new Object[DEFAULT_CAPACITY];
    }

    /**
     * Constructs an ArrayList with the specified capacity.
     *
     * @param capacity the initial capacity of the list
     * @throws IllegalArgumentException if the capacity is less than or equal to 0
     */
    @SuppressWarnings("unchecked")
    public ArrayList(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity should be a positive");
        }
        values = (T[]) new Object[capacity];
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * Adds the specified element to the end of the list.
     *
     * @param value the element to add
     */
    @Override
    public void add(T value) {
        if (size == values.length) {
            T[] newValues = Arrays.copyOf(values, size * 2);
            System.arraycopy(values, 0, newValues, 0, size);
            values = newValues;
        }
        values[size()] = value;
        size++;
    }

    /**
     * Adds the specified element at the specified index in the list.
     *
     * @param value the element to add
     * @param index the index at which to add the element
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public void add(T value, int index) {
        checkIndex(index);

        if (size == values.length) {
            T[] newValues = Arrays.copyOf(values, size * 2);
            System.arraycopy(values, 0, newValues, 0, index);
            newValues[index] = value;
            System.arraycopy(values, index, newValues, index + 1, size - index);
            values = newValues;
        } else {
            System.arraycopy(values, index, values, index + 1, size - index);
            values[index] = value;
        }
        size++;
    }

    /**
     * Returns the list size.
     *
     * @return the size of the list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the element at the specified index in the list.
     *
     * @param index the index of the element to retrieve
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public T get(int index) {
        checkIndex(index);
        return values[index];
    }

    /**
     * Removes the element from the list.
     *
     * @param value the element to remove
     */
    @Override
    public void remove(T value) {
        for (int i = 0; i < size; i++) {
            if (values[i].equals(value)) {
                System.arraycopy(values, i + 1, values, i, size - i - 1);
                values[size - 1] = null;
                size--;
                return;
            }
        }
    }

    /**
     * Clears the list, removing all elements.
     */
    @Override
    public void clear() {
        Arrays.fill(values, null);
        size = 0;
    }

    /**
     * Sorts the elements of the list according to natural order. The method is based on the quicksort algorithm.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void sort() {
        if (values[0] instanceof Comparable) {
            quickSort(values, 0, size - 1, (Comparator<? super T>) Comparator.naturalOrder());
        } else {
            throw new IllegalArgumentException("Set Comparator for incomparable objects");
        }
    }

    /**
     * Sorts the elements of the list using the specified comparator. The method is based on the quicksort algorithm.
     *
     * @param comparator the comparator to use for sorting
     */
    public void sort(Comparator<? super T> comparator) {
        quickSort(values, 0, size - 1, comparator);
    }

    private static <T> void quickSort(T[] array, int begin, int end, Comparator<? super T> comparator) {

        if (begin < end) {
            int partitionIndex = partition(array, begin, end, comparator);
            quickSort(array, begin, partitionIndex - 1, comparator);
            quickSort(array, partitionIndex + 1, end, comparator);
        }
    }

    private static <T> int partition(T[] array, int begin, int end, Comparator<? super T> comparator) {
        T pivot = array[end];
        int i = begin - 1;

        for (int j = begin; j < end; j++) {
            if (comparator.compare(array[j], pivot) <= 0) {
                i++;
                T temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        T temp = array[i+1];
        array[i+1] = array[end];
        array[end] = temp;
        return i + 1;
    }

    /**
     * Returns a string representation of the list.
     */
    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        IntStream.range(0, size).forEach(i -> {
            sb.append(values[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        });
        sb.append("]");
        return sb.toString();
    }
}
