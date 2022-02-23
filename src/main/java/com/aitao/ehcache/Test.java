package com.aitao.ehcache;


import com.aitao.model.Person;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @Company QAX
 * @Author : admin
 * @Create : 2022/1/26 11:45
 */
public class Test {
    public static void main(String[] args) {
        //创建缓存管理器
        CacheManager cacheManager = CacheManager.create("./src/main/resources/ehcache.xml");
        //获取缓存对象
        Cache cache = cacheManager.getCache("HelloWorldCache");
        //创建缓存key
        List<Element> list = new ArrayList<>();
        list.add(new Element("name_key1", "aitao"));
        list.add(new Element("name_key2", "lml"));
        list.add(new Element("person_obj", new Person(18, "aitao and lml")));
        //添加元素
        cache.putAll(list);
        //获取缓存
        Element nameKey1 = cache.get("name_key1");
        System.out.println("nameKey1:" + nameKey1);
        System.out.println("nameKey1.getObjectValue():" + nameKey1.getObjectValue());
        Element personObj = cache.get("person_obj");
        System.out.println("personObj.getObjectValue():" + personObj.getObjectValue());
        System.out.println("cache.getSize() before:" + cache.getSize());
        //删除缓存
        cache.remove("name_key1");
        //获取缓存大小
        System.out.println("cache.getSize() after:" + cache.getSize());
        //刷新缓存
        cache.flush();
        //关闭缓存管理器
        cacheManager.shutdown();
    }
}
