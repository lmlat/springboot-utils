package com.aitao.util.sort;

import java.util.Comparator;

/**
 * @Author : AiTao
 * @Create : 2021-12-21 0:54
 * @Description : 排序接口
 */
public interface ISort {
    /**
     * 数组排序
     *
     * @param arr 数组
     */
    <T extends Number> void sort(T[] arr);

    /**
     * 数组排序
     *
     * @param arr 数组
     */
    void sort(String[] arr);

    /**
     * 数组排序
     *
     * @param arr        数组
     * @param comparator 排序器
     * @param <T>        元素类型
     */
    <T> void sort(T[] arr, Comparator<T> comparator);

    /**
     * 元素交换
     *
     * @param array 数组
     * @param i     索引位置
     * @param j     索引位置
     */
    default <T> void swap(T[] array, int i, int j) {
        if (array == null || array.length == 0) {
            return;
        }
        T t = array[i];
        array[i] = array[j];
        array[j] = t;
    }

    /**
     * 元素交换（处理基本数据类型）
     *
     * @param array 数组
     * @param i     索引位置
     * @param j     索引位置
     */
    default void swap(byte[] array, int i, int j) {
        if (array == null || array.length == 0) {
            return;
        }
        byte tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    default void swap(short[] array, int i, int j) {
        if (array == null || array.length == 0) {
            return;
        }
        short tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    default void swap(char[] array, int i, int j) {
        if (array == null || array.length == 0) {
            return;
        }
        char tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    default void swap(int[] array, int i, int j) {
        if (array == null || array.length == 0) {
            return;
        }
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    default void swap(long[] array, int i, int j) {
        if (array == null || array.length == 0) {
            return;
        }
        long tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    default void swap(float[] array, int i, int j) {
        if (array == null || array.length == 0) {
            return;
        }
        float tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    default void swap(double[] array, int i, int j) {
        if (array == null || array.length == 0) {
            return;
        }
        double tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    default void swap(boolean[] array, int i, int j) {
        if (array == null || array.length == 0) {
            return;
        }
        boolean tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
}
