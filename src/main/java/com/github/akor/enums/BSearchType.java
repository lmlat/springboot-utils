package com.github.akor.enums;

/**
 * @Author : AiTao
 * @Create : 2021-10-24 13:49
 * @Description : B站搜索类型
 */
public enum BSearchType {
    B_ALL("综合", "all", ""),
    B_STATION_VIDEO("视频", "video", "video-item"),
    B_STATION_BANG_UMI("番剧", "bangumi", "bangumi-item"),
    B_STATION_PGC("影视", "pgc", "pgc-item"),
    B_STATION_LIVE("直播", "live", "live-user-item::live-room-item"),
    B_STATION_ARTICLE("专栏", "article", "article-item"),
    B_STATION_TOPIC("话题", "topic", "topic-item"),
    B_STATION_UP_USER("用户", "upuser", "user-item"),
    ;

    private final String desc;
    private final String value;
    private final String className;

    BSearchType(String desc, String value, String className) {
        this.desc = desc;
        this.className = className;
        this.value = value;
    }

    public String getClassName() {
        return className;
    }

    public String getDesc() {
        return desc;
    }

    public String getValue() {
        return value;
    }
}
