package com.aitao.util.sort;

import com.aitao.util.sort.strategy.BubbleSort;
import com.aitao.util.sort.strategy.InsertSort;

import java.util.Comparator;

/**
 * @Author : AiTao
 * @Create : 2021-12-21 0:57
 * @Description : 排序抽象类
 */
public class Sort implements ISort, ISwitchState {
    private ISort sort = new BubbleSort();
//    public Sort(SortStrategy sortStrategy) {
//        setStrategy(sortStrategy);
//    }
//
//    /**
//     * 设置排序策略
//     *
//     * @param sortStrategy 排序算法
//     */
//    public void setStrategy(SortStrategy sortStrategy) {
//        if (SortStrategy.BUBBLE == sortStrategy) {
//            sort = new BubbleSort<>();
//        } else if (SortStrategy.INSERT == sortStrategy) {
//            sort = new InsertSort<>();
//        }
//    }

    @Override
    public <T extends Number> void sort(T[] array) {
        sort.sort(array);
    }

    @Override
    public void sort(String[] array) {
        sort.sort(array);
    }

    @Override
    public <T> void sort(T[] array, Comparator<T> comparator) {
        sort.sort(array, comparator);
    }

    @Override
    public ISort bubble() {
        sort = new BubbleSort();
        return this;
    }

    @Override
    public ISort insert() {
        sort = new InsertSort();
        return this;
    }

    @Override
    public ISort normal() {
        sort = new BubbleSort();
        return this;
    }
}
