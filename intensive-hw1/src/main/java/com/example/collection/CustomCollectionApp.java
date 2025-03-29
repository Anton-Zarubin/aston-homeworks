package com.example.collection;

import com.example.collection.list.impl.ArrayList;

import java.util.Random;

public class CustomCollectionApp {
    public static void main(String[] args) {

        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(new Random().nextInt(100));
        }

        System.out.println("Демонстрация работы с ArrayList");

        System.out.println("-Исходные данные:" + list + ", size = " + list.size());

        list.add(101, 3);
        System.out.println("-После добавления 101 на позицию 3:" + list + ", size = " + list.size());

        list.add(101);
        System.out.println("-После добавления 101 в конец списка:" + list + ", size = " + list.size());

        list.remove(101);
        System.out.println("-После удаления 101:" + list + ", size = " + list.size());

        list.sort();
        System.out.println("-После сортировки:" + list + ", size = " + list.size());

        list.clear();
        System.out.println("-После очистки:" + list + ", size = " + list.size());
    }
}