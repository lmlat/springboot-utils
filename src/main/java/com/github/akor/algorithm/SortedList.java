package com.github.akor.algorithm;


import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Random;

/**
 * @Author : AiTao
 * @Create : 2021-12-22 2:33
 * @Description : 有序集合
 */
public class SortedList<E> {
    static final int DEFAULT_CAPACITY = 16;
    static final Object[] EMPTY_ELEMENT = {};
    transient Object[] elements;
    int size;


    public SortedList() {
        size = 0;
        elements = EMPTY_ELEMENT;
    }

    public SortedList(int size) {
        if (size > 0) {
            elements = new Object[size];
        } else if (size == 0) {
            elements = EMPTY_ELEMENT;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + size);
        }
    }

    public SortedList(Collection<E> collection) {
        elements = collection.toArray();
        if ((size = elements.length) != 0) {
            if (elements.getClass() != Object[].class) {
                elements = Arrays.copyOf(elements, size, Object[].class);
            }
        } else {
            elements = EMPTY_ELEMENT;
        }
    }

    private boolean add(E e, Comparator<E> comparator) {

        return true;
    }

    public boolean add(E e) {
        boolean flag = false;
        int leftIdx = 0, rightIdx = size - 1, mid;
        for (int currentIdx = 0; currentIdx < size; currentIdx++) {
            mid = leftIdx + ((rightIdx - leftIdx) >> 1);

        }
        //扩容
        elements[size++] = e;
        return true;
    }

    private final Random random = new SecureRandom();

    private int randomLevel() {
        double ins = random.nextDouble();
        int nextLevel = 0;
        if (ins > Math.E && 3 < 32) {
            nextLevel += 1;
        }
        return nextLevel;
    }

    public static void main(String[] args) {
        SortedList<Integer> sortedList = new SortedList<>();
        for (int i = 0; i < 10; i++) {
            System.out.println(sortedList.randomLevel());
        }
    }
}
