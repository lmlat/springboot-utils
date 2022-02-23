package com.aitao.util.sort.strategy;

import com.aitao.util.sort.ISort;

import java.util.Comparator;

public final class InsertSort implements ISort {
    @Override
    public <T extends Number> void sort(T[] array) {
        sort(array, Comparator.comparing(a -> Integer.valueOf(a.toString())));
    }

    @Override
    public void sort(String[] array) {
        sort(array, String::compareTo);
    }

    @Override
    public <T> void sort(T[] array, Comparator<T> comparator) {
        System.out.println("使用了插入排序");
        T temp;
        for (int i = 1, j; i < array.length; i++) {
            if (comparator.compare(array[i], array[i - 1]) < 0) {
                j = i;
                temp = array[i];
                while (j > 0 && comparator.compare(array[j - 1], temp) > 0) {
                    array[j] = array[j - 1];
                    j--;
                }
                array[j] = temp;
            }
        }
    }
}