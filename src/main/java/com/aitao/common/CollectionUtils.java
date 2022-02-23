package com.aitao.common;

import com.aitao.model.User;
import com.aitao.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @Author : AiTao
 * @Create : 2021-11-01 23:29
 * @Description :
 */
public class CollectionUtils {
    /**
     * 去重
     *
     * @param collection 数据源
     * @param comparator 排序器（根据什么字段或条件排序）
     * @param <T>        泛型
     * @return 返回去重后的数据集合
     */
    public static <T> List<T> distinct(Collection<T> collection, Comparator<T> comparator) {
        return collection.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(comparator)), ArrayList::new));
    }

    /**
     * 求交集
     *
     * @param srcMap  map1
     * @param destMap map2
     * @param <V>     value值类型
     * @return 返回map1与map2中相同元素的集合
     */
    public static <K, V> Map<K, V> intersect(Map<K, V> srcMap, Map<K, V> destMap) {
        Map<K, V> newMap = null;
        if (srcMap != null && !srcMap.isEmpty() && destMap != null && !destMap.isEmpty()) {
            Set<K> s1 = new HashSet<>(srcMap.keySet());
            s1.retainAll(new HashSet<>(destMap.keySet()));
            newMap = new HashMap<>();
            for (K key : s1) { //构造新Map
                newMap.put(key, srcMap.get(key));
            }
        }
        return newMap;
    }

    /**
     * 求交集
     *
     * @param c1  集合1
     * @param c2  集合2
     * @param <T> 集合数据类型
     * @return 返回c1与c2中相同元素的集合
     */
    public static <T> Collection<T> intersect(Collection<T> c1, Collection<T> c2) {
        c1.retainAll(c2);
        return c1;
    }

    /**
     * 计算最大值
     *
     * @param c   集合
     * @param <T> 类型
     * @return 返回集合元素中的最大值
     */
    public static <T extends Object & Comparable<? super T>> T max(Collection<T> c) {
        return ifMin(c, false);
    }

    /**
     * 计算最小值
     *
     * @param c   集合
     * @param <T> 类型
     * @return 返回集合元素中的最小值
     */
    public static <T extends Object & Comparable<? super T>> T min(Collection<T> c) {
        return ifMin(c, true);
    }

    /**
     * 计算最大或最小值
     *
     * @param c     集合
     * @param isMin 是否获取最小值
     * @param <T>   类型
     * @return isMin=true时返回最小值,否则返回最大值
     */
    private static <T extends Object & Comparable<? super T>> T ifMin(Collection<T> c, boolean isMin) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (c.isEmpty()) {
            throw new NoSuchElementException();
        }
        Iterator<T> iter = c.iterator();
        T min = iter.next(), max = min;
        while (iter.hasNext()) {
            T val = iter.next();
            if (min.compareTo(val) > 0) {
                min = val; //改变最小值
            }
            if (max.compareTo(val) < 0) {
                max = val; //记录最大值
            }
        }
        return isMin ? min : max;
    }

    //Test
    public static void main(String[] args) {
        List<Integer> c1 = ArrayUtils.asList(17);
        System.out.println(max(c1));
        System.out.println(min(c1));
        List<Integer> c2 = ArrayUtils.asList(18, 2, 3, 4, 5, 6, 7, 8, 9, 0, 11, 12, 13, 14, 15);
        //System.out.println(intersect(c1, c2));
        List<Integer> tmpIncludes = new ArrayList<>(c1);
        if (c1.retainAll(c2) && c1.isEmpty()) {
            c1 = tmpIncludes;
        }
        System.out.println(c1);

        List<User> userList = new ArrayList<>();
        Collections.addAll(userList, new User("aitao", "123"), new User("aitao", "123"), new User("aitao", "123"));
        System.out.println(distinct(userList, Comparator.comparing(User::getUsername)));
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(1, 1);
        map1.put(2, 2);
        Map<Integer, Integer> map2 = new HashMap<>();
        map2.put(1, 33);
        map2.put(2, 44);
        System.out.println(intersect(map1, map2));
    }
}
