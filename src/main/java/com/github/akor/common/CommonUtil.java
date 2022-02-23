package com.github.akor.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/09/24 16:43
 * @Description : 通用工具类
 */
public class CommonUtil {
    //去重容器
    private static Set<String> UNIQUE_CONTAINER = null;

    static {
        UNIQUE_CONTAINER = new HashSet<>();
    }

    /**
     * 随机生成字符串编码
     *
     * @param length 字符串编码长度
     * @return 返回唯一编码
     */
    public static String generateUniqueCode(int length) {
        String[] digitLetter = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
                "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "w", "x", "y", "z"
                , "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        StringBuilder res = new StringBuilder("BEE-");
        Random rd = new Random();
        for (int i = 0; i < length; i++) {
            res.append(digitLetter[rd.nextInt(digitLetter.length)]);
        }
        if (UNIQUE_CONTAINER.contains(res.toString())) {
            return generateUniqueCode(length);
        } else {
            return res.toString();
        }
    }

    /**
     * 生成指定区间内的随机数 [min, max]
     *
     * <pre>
     *     range(10, 1) = range(1, 10) = [1, 10]
     *     range(2, 4) = [2, 4]
     * </pre>
     *
     * @param min 左区间
     * @param max 右区间
     * @return 随机数
     */
    public static int range(int min, int max) {
        if (min == max) {
            return min;
        }
        if (min > max) {
            min ^= max;
            max ^= min;
            min ^= max;
        }
        return new SecureRandom().nextInt(max - min + 1) + min;
    }

    /**
     * 格式化百分比
     *
     * @param content 内容
     * @param bit     处理的数据 百分比数保留bit位小数， 20.00%
     */
    public static String toPercentage(double content, int bit) {
        return new DecimalFormat(buildFormatter(bit).append("%").toString()).format(content);
    }

    /**
     * 格式化小数
     *
     * @param content 内容
     * @param bit     处理的数据 百分比数保留bit位小数， 20.00%
     */
    public static String toDecimal(double content, int bit) {
        return new DecimalFormat(buildFormatter(bit).toString()).format(content);
    }

    public static String toDecimal(String content, int bit) {
        try {
            return toDecimal(new BigDecimal(content), bit);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("入参格式错误, content:" + content);
        }
    }

    public static String toDecimal(BigDecimal decimal, int bit) {
        return decimal.setScale(bit, BigDecimal.ROUND_HALF_UP).toString();//四舍五入
    }

    private static String buildCommend(String commend, String scriptPath) {
        return commend + " " + scriptPath;
    }

    public static void executeScript(String commend, String scriptPath) {
        executeScript(buildCommend(commend, scriptPath));
    }

    public static void executeScript(String commend) {
        try {
            System.out.println("start");
            Process pr = Runtime.getRuntime().exec(commend);
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            pr.waitFor();
            System.out.println("end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异常堆栈信息
     * <pre>
     *     getStackTrace(null) = ""
     *     getStackTrace(new Exception("error")) = 异常堆栈信息
     * </pre>
     *
     * @param throwable 抛出的异常
     * @return 异常堆栈信息
     */
    public static String getStackTrace(Throwable throwable) {
        if (throwable != null) {
            StringWriter sw = new StringWriter();
            try (PrintWriter pw = new PrintWriter(sw)) {
                throwable.printStackTrace(pw);
                return sw.toString();
            }
        }
        return "";
    }

    /**
     * 构造格式化器
     */
    private static StringBuilder buildFormatter(int bit) {
        StringBuilder formatter = new StringBuilder("0");
        for (int i = 0; i < bit; i++) {
            formatter.append(i == 0 ? ".0" : "0");
        }
        return formatter;
    }

    public static void main(String[] args) {
        System.out.println(toDecimal(-1111.111, 2));
        System.out.println(toDecimal("1111#.222111", 2));
        System.out.println(toPercentage(0.6, 2));
        for (int i = 0; i < 10; i++) {
            System.out.println(range(10, 1));
        }
        System.out.println(getStackTrace(new Exception("哇塞")));
        executeScript("python D:\\Learn\\springboot-utils\\src\\main\\resources\\test.py");
    }
}
