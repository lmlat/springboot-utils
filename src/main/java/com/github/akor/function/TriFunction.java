package com.github.akor.function;

/**
 * Author : AiTao Create : 2021/3/12 3:34 Description : 封装Redis工具类
 */
@FunctionalInterface
interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}
