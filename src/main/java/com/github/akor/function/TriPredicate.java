package com.github.akor.function;

/**
 * Author : AiTao
 * Create : 2021/3/12 17:18
 * Create : 2021/3/12 17:18
 * Description :
 */
@FunctionalInterface
interface TriPredicate<T, U, V> {
    boolean test(T t, U u, V v);
}
