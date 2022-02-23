package com.github.akor.test;

import com.github.akor.model.User;
import com.github.akor.util.ArrayUtils;
import com.github.akor.util.sort.ISort;
import com.github.akor.util.sort.Sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @Author : AiTao
 * @Create : 2021-12-21 1:36
 * @Description :
 */
public class TestSort {
    public static void main(String[] args) {
        String[] strs = ArrayUtils.newArray("c", "b", "a", "f");
        User[] users = ArrayUtils.newArray(new User("d", "123"), new User("b", "123"), new User("a", "123"));

        Integer[] arr = new Integer[]{2, 1, 4, 0, 3, 9};
        Sort sort = new Sort();
        // 这里可以选择不同的策略完成排序
//        sort.setStrategy(SortStrategy.INSERT);
        sort.sort(arr);
        // 输出 [0, 1, 2, 3, 4, 9]
        System.out.println(Arrays.toString(arr));

        ISort sort1 = new Sort().insert();
        sort1.sort(users, Comparator.comparing(User::getUsername));
        System.out.println(Arrays.toString(users));

        ISort sort2 = new Sort();
        sort2.sort(strs, Comparator.reverseOrder());
        System.out.println(Arrays.toString(strs));
    }
}
