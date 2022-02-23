package com.github.akor.esayexcel;

import com.github.akor.model.User;
import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : AiTao
 * @Create : 2021-10-21 19:09
 * @Description : easyexcel测试
 */
public class TestEasyExcel {
    public static void main(String[] args) {
//        EasyExcel.write("C:/user.xlsx", User.class).sheet("学生列表").doWrite(getData());
        EasyExcel.read("C:/user.xlsx", User.class, new ExcelListener()).sheet("学生列表").doRead();
    }

    private static List<User> getData() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setAge(String.valueOf(i));
            user.setUsername("aitao" + i);
            user.setPaaswod("123456" + i);
            userList.add(user);
        }
        return userList;
    }
}
