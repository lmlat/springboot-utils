package com.github.akor.model;

import com.github.akor.annotation.IsDate;
import com.alibaba.excel.annotation.ExcelProperty;

import java.io.Serializable;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/07/23 10:13
 * @Description :
 */
public class User implements Serializable {
    @ExcelProperty(value = "用户名", index = 0)
    private String username;
    @ExcelProperty(value = "密码", index = 1)
    private String paaswod;
    @ExcelProperty(value = "年龄", index = 2)
    public String age;
    @IsDate
    private String date;

    public User() {

    }

    public User(String username, String paaswod) {
        this.username = username;
        this.paaswod = paaswod;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPaaswod() {
        return paaswod;
    }

    public void setPaaswod(String paaswod) {
        this.paaswod = paaswod;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", paaswod='" + paaswod + '\'' +
                ", age='" + age + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
