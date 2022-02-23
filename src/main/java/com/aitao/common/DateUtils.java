package com.aitao.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);
    //日期格式管理器
    private static Map<String, DateTimeFormatter> dateTimeFormatterManager;

    static {
        initDateTimeFormatterManager();
    }

    /**
     * 初始化日期格式管理器
     */
    private static void initDateTimeFormatterManager() {
        dateTimeFormatterManager = new HashMap<>();
        dateTimeFormatterManager.put("default", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));//默认格式
        dateTimeFormatterManager.put("yyyy-MM-dd HH:mm:ss", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        dateTimeFormatterManager.put("yyMMddHHmmss", DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        dateTimeFormatterManager.put("yyyy年MM月dd日 HH时mm分ss秒", DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒"));
        dateTimeFormatterManager.put("yyyy年MM月dd日 HH:mm:ss", DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));
        dateTimeFormatterManager.put("yyyy-MM-dd HH:mm:ss E", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss E"));
        dateTimeFormatterManager.put("yyyy年MM月dd日 HH时mm分ss秒 E", DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒 E"));
        //时 分 秒置0
        dateTimeFormatterManager.put("yyyy-MM-dd 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
        dateTimeFormatterManager.put("yyyy年MM月dd日 00:00:00", DateTimeFormatter.ofPattern("yyyy年MM月dd日 00:00:00"));
        //时 分 秒置23:59:59
        dateTimeFormatterManager.put("yyyy-MM-dd 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59"));
        dateTimeFormatterManager.put("yyyy年MM月dd日 23:59:59", DateTimeFormatter.ofPattern("yyyy年MM月dd日 23:59:59"));
        dateTimeFormatterManager.put("yyyy-MM-dd", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dateTimeFormatterManager.put("yyyy年MM月dd日", DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
        dateTimeFormatterManager.put("yyyyMMdd", DateTimeFormatter.ofPattern("yyyyMMdd"));
        dateTimeFormatterManager.put("yyyy-MM-dd HH:mm", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        dateTimeFormatterManager.put("yyyy-MM-dd HH", DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
        dateTimeFormatterManager.put("yyyy年MM月dd日 E", DateTimeFormatter.ofPattern("yyyy年MM月dd日 E"));
        dateTimeFormatterManager.put("yyyy-MM-dd E", DateTimeFormatter.ofPattern("yyyy-MM-dd E"));
        dateTimeFormatterManager.put("yyyy-MM", DateTimeFormatter.ofPattern("yyyy-MM"));
        dateTimeFormatterManager.put("yyMMdd", DateTimeFormatter.ofPattern("yyMMdd"));
        dateTimeFormatterManager.put("yyyy", DateTimeFormatter.ofPattern("yyyy"));
        dateTimeFormatterManager.put("yy", DateTimeFormatter.ofPattern("yy"));
        dateTimeFormatterManager.put("MM", DateTimeFormatter.ofPattern("MM"));
        dateTimeFormatterManager.put("dd", DateTimeFormatter.ofPattern("dd"));
        dateTimeFormatterManager.put("HH", DateTimeFormatter.ofPattern("HH"));
        dateTimeFormatterManager.put("mm", DateTimeFormatter.ofPattern("mm"));
        dateTimeFormatterManager.put("ss", DateTimeFormatter.ofPattern("ss"));
        dateTimeFormatterManager.put("SSS", DateTimeFormatter.ofPattern("SSS"));
        dateTimeFormatterManager.put("E", DateTimeFormatter.ofPattern("E"));
        dateTimeFormatterManager.put("HH:mm:ss", DateTimeFormatter.ofPattern("HH:mm:ss"));
        dateTimeFormatterManager.put("HH时mm分ss秒", DateTimeFormatter.ofPattern("HH时mm分ss秒"));
        dateTimeFormatterManager.put("HHmmss", DateTimeFormatter.ofPattern("HHmmss"));
    }

    /**
     * 获取日期格式
     *
     * @param format 日期格式, yyyy-MM-dd HH:mm:ss
     * @return 返回DateTimeFormatter格式化器
     */
    public static DateTimeFormatter getFormatter(String format) {
        if (format == null || format.isEmpty() || "default".equals(format)) {
            return dateTimeFormatterManager.get("default");
        }
        // computeIfAbsent 处理自定义日期格式
        return dateTimeFormatterManager.computeIfAbsent(format, key -> DateTimeFormatter.ofPattern(format));
    }

    /**
     * 将Date转换成String
     *
     * @param date 日期
     * @return 返回字符串形式的日期
     */
    public static String toString(Date date) {
        return toString(date, getFormatter("default"));
    }

    public static String toString(Date date, String formatter) {
        return toString(date, getFormatter(formatter));
    }

    public static String toString(Date date, DateTimeFormatter formatter) {
        return asLocalDateTime(date).format(formatter);
    }

    /**
     * 将Date转换成LocalDateTime
     *
     * @param date 日期
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 将Date转换成LocalDate
     *
     * @param date 日期
     */
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 将Date转换成LocalTime
     *
     * @param date 日期
     */
    public LocalTime asLocalTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * 是否满足匹配当前时间字符串格式
     *
     * @return true匹配, false不匹配
     */
    private static boolean isDateTimeFormatter(String dateStr, DateTimeFormatter currentFormatter) {
        try {
            LocalDateTime.parse(dateStr, currentFormatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isDateFormatter(String dateStr, DateTimeFormatter currentFormatter) {
        try {
            LocalDate.parse(dateStr, currentFormatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isTimeFormatter(String dateStr, DateTimeFormatter currentFormatter) {
        try {
            LocalTime.parse(dateStr, currentFormatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 字符串转换Date
     *
     * @param dateStr   日期字符串
     * @param formatter 格式化器
     * @return 日期对象
     */
    public static Date asDate(String dateStr, DateTimeFormatter formatter) {
        return asDate(LocalDateTime.parse(dateStr, formatter));
    }

    /**
     * LocalDate转Date
     *
     * @param localDate LocalDate
     */
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime转Date
     *
     * @param localDateTime LocalDateTime
     */
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 重置日期时间为00:00:00
     *
     * @return 返回一天的开始时间
     */
    public static String resetDayStartTime(Date date) {
        return toString(date, getFormatter("yyyy-MM-dd 00:00:00"));
    }

    /**
     * 重置日期时间为23:59:59
     *
     * @return 返回一天的结束时间
     */
    public static String resetDayEndTime(Date date) {
        return toString(date, getFormatter("yyyy-MM-dd 23:59:59"));
    }

    /**
     * 获取系统当前时间戳
     *
     * @return 1566889186583
     */
    public static long toTimestamp() {
        return Instant.now().atOffset(ZoneOffset.ofHours(8)).getSecond();
    }

    public static String toTimestamp(Date date) {
        return String.format("%tQ", date);
    }

    public static void main(String[] args) {
        System.out.println(toTimestamp());
        System.out.println(toTimestamp(new Date()));
        System.out.printf("%tQ%n", new Date());
        System.out.println(asDate("2021-02-02 11:22:33", getFormatter("yyyy-MM-dd HH:mm:ss")));
//        System.out.println(String.format("%tp", asDate("2021-09-28", getFormatter("yyyy-MM-dd"))));

    }
}

