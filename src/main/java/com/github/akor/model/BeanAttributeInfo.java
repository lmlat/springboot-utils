package com.github.akor.model;

import java.lang.reflect.Method;

/**
 * @Author : AiTao
 * @Create : 2021-11-02 19:23
 * @Description : 存放实体类的属性信息
 */
public class BeanAttributeInfo {
    //字段名
    private String fieldName;
    //字段类型
    private Object fieldType;
    //字段值的getter方法
    private Method getMethod;
    //字段值的setter方法
    private Method setMethod;
    //是否公共化
    private boolean isPublic;
    //是否保护化
    private boolean isProtected;
    //是否私有化
    private boolean isPrivate;

    public BeanAttributeInfo(String fieldName, Object fieldType, Method getMethod, Method setMethod, boolean isPublic, boolean isProtected, boolean isPrivate) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.getMethod = getMethod;
        this.setMethod = setMethod;
        this.isPublic = isPublic;
        this.isProtected = isProtected;
        this.isPrivate = isPrivate;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldType() {
        return fieldType;
    }

    public void setFieldType(Object fieldType) {
        this.fieldType = fieldType;
    }

    public Method getGetMethod() {
        return getMethod;
    }

    public void setGetMethod(Method getMethod) {
        this.getMethod = getMethod;
    }

    public Method getSetMethod() {
        return setMethod;
    }

    public void setSetMethod(Method setMethod) {
        this.setMethod = setMethod;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public void setProtected(boolean aProtected) {
        isProtected = aProtected;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @Override
    public String toString() {
        return "BeanAttributeInfo{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldType=" + fieldType +
                ", getMethod=" + getMethod +
                ", setMethod=" + setMethod +
                ", isPublic=" + isPublic +
                ", isProtected=" + isProtected +
                ", isPrivate=" + isPrivate +
                '}';
    }
}
