package com.github.akor.enums;

/**
 * @Author : AiTao
 * @Create : 2021-10-24 14:19
 * @Description : 站点类型枚举
 */
public enum SiteType {
    B_STATION("B站"),
    QIANKUTU("千库图");
    private final String desc;

    SiteType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
