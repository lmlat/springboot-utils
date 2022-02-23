package com.github.akor.util.sort.strategy;

import com.github.akor.util.sort.ISort;

import java.util.Comparator;

/**
 * @Author : AiTao
 * @Create : 2021-12-21 1:00
 * @Description : 冒泡排序算法
 */
public final class BubbleSort implements ISort {
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
        System.out.println("使用了冒泡排序");
        boolean flag;
        for (int i = 0; i < array.length - 1; i++) {
            flag = false;
            for (int j = 0; j < array.length - i - 1; j++) {
                if (comparator.compare(array[j], array[j + 1]) > 0) {
                    swap(array, j, j + 1);
                    flag = true;
                }
            }
            if (!flag) {
                break;
            }
        }
    }
}
