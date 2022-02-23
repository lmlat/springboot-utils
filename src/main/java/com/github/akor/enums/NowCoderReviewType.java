package com.github.akor.enums;

/**
 * @Author : AiTao
 * @Create : 2021-10-24 22:24
 * @Description : 牛客面试题库类型枚举
 */
public enum NowCoderReviewType {
    JAVA("review-java", "Java校招面试题目合集", 6, 30000),
    TEST("review-test", "测试校招面试题目合集", 21, 30000),
    PRODUCT("review-product", "产品&运营面试题库", 44, 30000),
    FRONTEND("review-frontend", "前端校招面试题目合集", 26, 30000),
    ML("review-ml", "算法/机器学习校招面试题目合集", 20, 30000),
    C("review-c", "c++校招面试题目合集", 15, 30000),
    NETWORK("review-network", "网络基础常考面试题", 1, 30000),
    JS("front-end-interview", "JS面试经典题目合集", 4, 30000),
    NINE_CHAPTER("nine-chapter", "九章算法", 4, 30000),
    ;

    private final String keyword;//关键词
    private final String specialColumn;//专栏
    private final Integer pageSize;//页数
    private final Integer parseInterval;//解析间隔时间

    NowCoderReviewType(String keyword, String specialColumn, Integer pageSize, Integer parseInterval) {
        this.keyword = keyword;
        this.specialColumn = specialColumn;
        this.pageSize = pageSize;
        this.parseInterval = parseInterval;
    }

    public String getSpecialColumn() {
        return specialColumn;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getParseInterval() {
        return parseInterval;
    }

    public String getKeyword() {
        return keyword;
    }
}
