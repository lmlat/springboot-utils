package com.aitao.web.response;

import com.aitao.enums.WebStatus;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Component
public class WebResponse<T> implements Serializable {
    /**
     * 响应成功状态
     */
    private boolean success;

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    public static <T> WebResponse<T> success() {
        return success(null);
    }

    public static <T> WebResponse<T> success(T data) {
        return success(WebStatus.SUCCESS.getMessage(), data);
    }

    public static <T> WebResponse<T> success(String message) {
        return success(message, null);
    }

    public static <T> WebResponse<T> success(String message, T data) {
        return success(WebStatus.SUCCESS.getCode(), message, data);
    }

    public static <T> WebResponse<T> success(int code, String message) {
        return success(code, message, null);
    }

    public static <T> WebResponse<T> success(int code, String message, T data) {
        WebResponse<T> webResponse = new WebResponse<T>();
        webResponse.setCode(code);
        webResponse.setMessage(message);
        webResponse.setSuccess(true);
        return webResponse;
    }

    public static <T> WebResponse<T> failure() {
        return failure(WebStatus.SUCCESS.getMessage());
    }

    public static <T> WebResponse<T> failure(String message) {
        return failure(message, null);
    }

    public static <T> WebResponse<T> failure(String message, T data) {
        return failure(WebStatus.SERVER_ERROR.getCode(), message, data);
    }

    public static <T> WebResponse<T> failure(int code, String message) {
        return failure(WebStatus.SERVER_ERROR.getCode(), message, null);
    }

    public static <T> WebResponse<T> failure(int code, String message, T data) {
        WebResponse<T> R = new WebResponse<>();
        R.code = code;
        R.data = data;
        R.success = false;
        R.message = message;
        return R;
    }

    public static <T> WebResponse<T> decide(boolean b) {
        return decide(b, WebStatus.SUCCESS.getMessage(), WebStatus.SERVER_ERROR.getMessage());
    }

    public static <T> WebResponse<T> decide(boolean b, T data) {
        return decide(b, data, WebStatus.SUCCESS.getMessage(), WebStatus.SERVER_ERROR.getMessage());
    }

    public static <T> WebResponse<T> decide(boolean b, String success, String failure) {
        return b ? success(success) : failure(failure);
    }

    public static <T> WebResponse<T> decide(boolean b, T data, String success, String failure) {
        return b ? success(success, data) : failure(failure, data);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}


