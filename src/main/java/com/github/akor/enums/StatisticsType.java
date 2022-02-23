package com.github.akor.enums;

/**
 * @Author : AiTao
 * @Create : 2021-10-29 18:16
 * @Description : 统计类型枚举
 */
public enum StatisticsType {
    LIKE((byte) 1, "点赞"),
    DISLIKE((byte) 0, "取消点赞/未点赞"),
    COLLECT((byte) 1, "收藏"),
    CANCEL_COLLECT((byte) 0, "取消收藏/未收藏"),
    SHARE((byte) 1, "分享"),
    CANCEL_SHARE((byte) 0, "取消分享/未分享"),
    FOCUS((byte) 1, "关注"),
    CANCEL_FOCUS((byte) 0, "取消关注/未关注"),
    COMMENT((byte) 1, "评论"),
    CANCEL_COMMENT((byte) 0, "取消评论/未评论"),
    UNKNOWN((byte) -1, "未知");

    private final String message;
    private final byte status;

    StatisticsType(byte status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public byte getStatus() {
        return status;
    }

    public static StatisticsType getType(String name) {
        for (StatisticsType currentType : StatisticsType.values()) {
            if (currentType.name().equalsIgnoreCase(name)) {
                return currentType;
            }
        }
        return UNKNOWN;
    }
}
