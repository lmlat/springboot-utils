package com.aitao.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

public class Person implements Serializable {

    @JSONField(name = "AGE")
    private int age;

    @JSONField(name = "FULL NAME")
    private String fullName;

    @JSONField(name = "DATE OF BIRTH", format = "yyyy-MM-dd HH:mm:ss")
    private Date dateOfBirth;

    public Person() {

    }

    public Person(int age, String fullName) {
        super();
        this.age = age;
        this.fullName = fullName;
    }

    public Person(int age, String fullName, Date dateOfBirth) {
        super();
        this.age = age;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", fullName='" + fullName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}