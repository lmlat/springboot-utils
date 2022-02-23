package com.github.akor.test;

import com.github.akor.files.JsonUtils;
import com.github.akor.model.Person;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/07/23 10:04
 * @Description :
 */
public class TestJsonUtil {
    public static void main(String[] args) {
//        Set<String> set = new HashSet<>();
//        set.add("1");
//        set.add("2");
//        System.out.println(set);
//        set.clear();
//        System.out.println(set);
//        toJsonString();
        testBeanToJSONObject();
//        testToBeanMap();
        testNewJson();
    }

    private static void testNewJson() {
        Person person = new Person(20, "John", new Date());
        Person person2 = new Person(21, "John1", new Date());
        System.out.println(JsonUtils.newJson(person));
        System.out.println(JsonUtils.newJsons(Arrays.asList(person, person2)));
        System.out.println(JsonUtils.newJsons(person, person2));
    }

    private static void testToBeanMap() {
        List<Person> list = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            list.add(new Person(i, "aitao" + i));
            list2.add(i);
        }
        list2.add(5);
        list2.add(5);
        list2.add(5);
        list2.add(5);
        System.out.println("============String->json================");
        String json = JsonUtils.toString(list);
        String json2 = JsonUtils.toString(list2);
        System.out.println(json);
        System.out.println(json2);
        System.out.println("============json->List================");
        System.out.println(JsonUtils.toList(json));
        System.out.println(JsonUtils.toList(json2));
        System.out.println("============json->Set================");
        System.out.println(JsonUtils.toSet(json2));
        System.out.println("============json->JSONArray================");
        System.out.println(JsonUtils.deSerialize(json2, JSONArray.class));
        System.out.println("============json->ListMap================");
        System.out.println(JsonUtils.toListMap(json));
    }

    private static void testBeanToJSONObject() {
        Person person = new Person(20, "John", new Date());
        Person person2 = new Person(21, "John1", new Date());
        List<Person> personList = Arrays.asList(person);
        String json = JsonUtils.toString(person);
        System.out.println("==============toMap===================");
        Map<String, Person> mapPerson = JsonUtils.toMap(json);
        System.out.println("toOneListMap:" + JsonUtils.toMap(JsonUtils.toString(personList)));
        System.out.println("toMap:" + mapPerson);
        System.out.println("toMap:" + (JsonUtils.toMap(json) instanceof Map));
        System.out.println(mapPerson.get("DATE OF BIRTH"));
        System.out.println(mapPerson.get("FULL NAME"));
        System.out.println(mapPerson.get("AGE"));
        System.out.println("==============toBean===================");
        //单个对象转 JSONArray,需要加前缀"["和后缀"]"
        System.out.println("toBean:" + JsonUtils.deSerialize(json, JSONObject.class));
        System.out.println("toBean:" + JsonUtils.deSerialize(json, Person.class));
        System.out.println("toBean:" + JsonUtils.deSerialize(json, JSONArray.class));
        System.out.println("==============toListMap===================");
        System.out.println("toListMap:" + JsonUtils.toListMap(json));
        System.out.println("toListMap:" + JsonUtils.toListMap(JsonUtils.toString(Arrays.asList(person, person2))));
        System.out.println("toListMap:" + JsonUtils.toListMap(JsonUtils.newJsons(Arrays.asList(person, person2))));
    }

    public static void toJsonString() {
        List<Person> personList = new ArrayList<>();
        Person person = new Person(22, "aitao");
        for (int i = 0; i < 5; i++) {
            personList.add(new Person(i, "aitao" + (i + 1), new Date()));
        }
        System.out.println("============Date->json================");
        System.out.println(JsonUtils.toString(new Date()));
        System.out.println("============Object->json================");
        System.out.println(JsonUtils.toString(person));
        System.out.println("============List->json================");
        System.out.println(JsonUtils.toString(personList));
    }
}
