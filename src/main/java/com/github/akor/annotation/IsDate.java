package com.github.akor.annotation;

import com.github.akor.annotation.helper.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author : AiTao
 * @Create : 2021-12-10 17:34
 * @Description : 字段日期校验器
 */
@Inherited
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = DateValidator.class)
@Documented
public @interface IsDate {
    /**
     * 日期格式
     */
    String format() default "yyyy-MM-dd HH:mm:ss";

    /**
     * 是否比当前时间晚
     */
    boolean future() default false;

    /**
     * 是否比当前时间早
     */
    boolean past() default false;

    /**
     * 验证失败返回信息
     */
    String message() default "必须为日期字符串,格式:{format}";

    /**
     * 验证分组
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
