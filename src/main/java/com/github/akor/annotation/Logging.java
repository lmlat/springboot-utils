package com.github.akor.annotation;

import com.github.akor.enums.LogBusinessType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author : AiTao
 * @Create : 2021-12-10 17:34
 * @Description : 日志注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Logging {

    /**
     * 默认无参输入
     */
    String value() default "暂无标题";

    /**
     * Title 默认输入
     */
    String title() default "暂无标题";

    /**
     * Describe 默认输入
     */
    String describe() default "暂无介绍";

    /**
     * 业 务 类 型  默认Query
     */
    LogBusinessType type() default LogBusinessType.QUERY;
}
