package com.aitao.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/08/03 16:12
 * @Description : Spring工具类
 */
@Component
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    //注入application值
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (Objects.isNull(SpringUtil.applicationContext)) {
            SpringUtil.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContent() {
        return applicationContext;
    }

    /**
     * 根据name获取bean对象
     *
     * @param name bean注入容器的名称
     * @return Object
     */
    public static Object getBean(String name) {
        return getApplicationContent().getBean(name);
    }

    /**
     * 根据name获取bean对象
     *
     * @param name  bean注入容器的名称
     * @param clazz 希望返回的对象类型
     * @param <T>   任意类型
     * @return T
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContent().getBean(name, clazz);
    }
}
