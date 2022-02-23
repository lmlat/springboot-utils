package com.github.akor.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/09/15 11:11
 * @Description : Excel报表导出
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelExportReport {
    //导出属性字段
    String property();

    //导出头部标题
    String title();

    //处理日期格式对象
    String dateformat() default "yyyy-MM-dd HH:mm:ss";

    //百分比,与decimal同时存在时会失效
    int percentage() default -1;

    //小数
    int decimal() default -1;
}
