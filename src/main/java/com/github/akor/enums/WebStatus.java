package com.github.akor.enums;

import com.github.akor.common.DateUtils;

import java.util.Date;

/**
 * Author : AiTao
 * Date : 2020/11/5
 * Time : 9:51
 * Information : 响应状态码
 */
public enum WebStatus {
    /**
     * 返回码和状态说明
     */
    BAD_REQUEST(Const.BAD_REQUEST, "参数不完整或者语法错误"),
    UNAUTHORIZED(Const.UNAUTHORIZED, "认证失败"),
    LOGIN_ERROR(Const.UNAUTHORIZED, "我国vi失败,用户名或密码无效"),
    FORBIDDEN(Const.FORBIDDEN, "权限不足,禁止访问"),
    NOT_FOUND(Const.NOT_FOUND, "请求的资源不存在"),
    OPERATE_ERROR(Const.BAD_METHOD, "操作失败,请求操作的资源不存在"),
    TIME_OUT(Const.TIME_OUT, "请求超时"),
    SUCCESS(Const.SUCCESS, "操作成功"),
    SERVER_ERROR(Const.ERROR, "服务器内部错误");


    /**
     * 响应信息：
     * {
     * "code": 200,
     * "data": "OK",
     * "message": "success",
     * "date": "2021-03-24 22:22:34",
     * "author: "AiTao"
     * }
     */
    //状态码
    private final int code;
    //状态描述信息
    private final String message;
    //日期时间
    private final String date;
    //作者
    private final String author;
    private final String count;

    WebStatus(int code, String message) {
        this.code = code;
        this.message = message;
        this.count = null;
        this.author = "AiTao";
        this.date = DateUtils.toString(new Date());
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public String getCount() {
        return count;
    }
}

