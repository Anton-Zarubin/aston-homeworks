package com.example.collection.list.impl;

import com.example.collection.list.List;

import java.util.Comparator;
import java.util.Objects;

/**
 * Implementation of a doubly linked list.
 *
 * @param <T> the type of elements in the list
 */
public class LinkedList<T> implements List<T> {

    /**
     * The current size of the data structure, representing the number of elements it contains.
     */
    private int size;

    /**
     * The first node in the linked structure.
     */
    private Node<T> head;

    /**
     * The last node in the linked structure.
     */
    private Node<T> tail;

    /**
     * Constructs an empty linked list.
     */
    public LinkedList() {
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
        Node<T> prev = tail;
        Node<T> newNode = new Node<>(value, prev, null);
        tail = newNode;
        if (prev == null) {
            head = newNode;
        } else {
            prev.next = newNode;
        }
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

        if (index == 0) {
            addFirst(value);
        } else {
            Node<T> prevNode = getNode(index - 1);
            Node<T> nextNode = prevNode.getNext();
            Node<T> newNode = new Node<>(value, prevNode, nextNode);
            prevNode.setNext(newNode);
            nextNode.setPrev(newNode);
            size++;
        }
    }

    /**
     * Adds the specified element to the beginning of the list.
     *
     * @param value the element to add
     */
    private void addFirst(T value) {
        Node<T> next = head;
        Node<T> newNode = new Node<>(value, null, next);
        head = newNode;
        if (next == null) {
            tail = newNode;
        } else {
            next.prev = newNode;
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
        return getNode(index).element;
    }

    /**
     * Removes the element from the list.
     *
     * @param value the element to remove
     */
    @Override
    public void remove(T value) {
        Node<T> currentNode = head;

        while (currentNode != null) {
            if (Objects.equals(currentNode.element, value)) {
                if (currentNode == head) {
                    head = head.next;
                    if (head != null) {
                        head.prev = null;
                    } else {
                        tail = null;
                    }
                } else if (currentNode == tail) {
                    tail = tail.prev;
                    if (tail != null) {
                        tail.next = null;
                    } else {
                        head = null;
                    }
                } else {
                    currentNode.prev.next = currentNode.next;
                    currentNode.next.prev = currentNode.prev;
                }
                size--;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    /**
     * Clears the list.
     */
    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Sorts the elements of the list according to natural order. The method is based on the mergesort algorithm.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void sort() {
        if (head.element instanceof Comparable) {
            sort((Comparator<? super T>) Comparator.naturalOrder());
        } else {
            throw new IllegalArgumentException("Set Comparator for incomparable objects");
        }
    }

    /**
     * Sorts the elements of the list using the specified comparator. The method is based on the mergesort algorithm.
     *
     * @param comparator the comparator to use for sorting
     */
    public void sort(Comparator<? super T> comparator) {
        head = mergeSort(head, comparator);
    }

    private Node<T> mergeSort(Node<T> node, Comparator<? super T> comparator) {
        if (node == null || node.next == null) {
            return node;
        }

        Node<T> middle = getMiddle(node);
        Node<T> secondHalf = middle.next;
        middle.next = null;

        Node<T> left = mergeSort(node, comparator);
        Node<T> right = mergeSort(secondHalf, comparator);

        return merge(left, right, comparator);
    }

    private Node<T> getMiddle(Node<T> node) {
        if (node == null) return null;
        Node<T> slow = node, fast = node;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    private Node<T> merge(Node<T> left, Node<T> right, Comparator<? super T> comparator) {
        Node<T> dummy = new Node<>(null, null, null);
        Node<T> current = dummy;

        while (left != null && right != null) {
            if (comparator.compare(left.element, (right.element)) <= 0) {
                current.next = left;
                left = left.next;
            } else {
                current.next = right;
                right = right.next;
            }
            current = current.next;
        }

        current.next = (left != null) ? left : right;
        return dummy.next;
    }

    /**
     * Node class represents a node in the linked list.
     *
     * @param <T> the type of element in the node
     */
    static class Node<T> {

        private T element;

        private Node<T> next;

        private Node<T> prev;

        /**
         * Constructs a node with the specified element, previous node and next node.
         *
         * @param element the element in the node
         * @param prev the previous node
         * @param next the next node
         */
        Node(T element, Node<T> prev, Node<T> next) {
            this.element = element;
            this.prev = prev;
            this.next = next;
        }

        public T getElement() {
            return element;
        }

        public void setElement(T element) {
            this.element = element;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public Node<T> getPrev() {
            return prev;
        }

        public void setPrev(Node<T> prev) {
            this.prev = prev;
        }
    }

    /**
     * Gets the node at the specified index.
     *
     * @param index the index of the node to retrieve
     * @return the node at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    private Node<T> getNode(int index) {
        checkIndex(index);

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current;
    }

    /**
     * Returns a string representation of the list.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;

        while (current != null) {
            sb.append(current.element);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
