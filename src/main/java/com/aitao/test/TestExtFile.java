package com.aitao.test;

import com.aitao.beans.ExtFile;

/**
 * @Company QAX
 * @Author : admin
 * @Create : 2022/1/26 11:29
 */
public class TestExtFile {
    public static void main(String[] args) {
        ExtFile file = new ExtFile("C:\\Users\\10034\\Desktop\\Learn\\springboot-utils\\src\\main\\resources\\static\\application.properties");
        System.out.println(file.getType());
        System.out.println(file.getMimeType());
    }
}
