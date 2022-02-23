package com.github.akor.common;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/09/29 12:50
 * @Motto : 投入得越多，就能得到越多得价值
 * @Dependent : 无
 * @Description :
 */
public class StringUtils {
    /**
     * 字符串判空校验
     *
     * @param str 字符串
     * @return true字段为空，false字段非空
     */
    public static boolean isEmpty(CharSequence str) {
        return Checks.isEmpty(str.toString());
    }

    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(CharSequence str) {
        return Checks.isBlank(str);
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    /**
     * 字符串分割（StringTokens令牌实现）
     *
     * @param content   分割内容
     * @param separator 多个分隔符号
     * @return 返回对象数组
     */
    public static List<String> split(String content, String separator) {
        if (isBlank(content)) {
            throw new NullPointerException("content值为空!!!");
        }
        StringTokenizer st = new StringTokenizer(content, separator);
        List<String> result = new ArrayList<>();
        //检查是否有还存在令牌
        while (st.hasMoreTokens()) {
            result.add(st.nextToken());//获取下一个令牌
        }
        return result;
    }

    /**
     * 生成一个UUID
     * <p>
     * UUID由以下几部分的组合：
     * （1）当前日期和时间，UUID的第一个部分与时间有关，如果你在生成一个UUID之后，过几秒又生成一个UUID，则第一个部分不同，其余相同。
     * （2）时钟序列。
     * （3）全局唯一的IEEE机器识别号，如果有网卡，从网卡MAC地址获得，没有网卡以其他方式获得。
     * </p>
     * <pre>
     *
     * </pre>
     *
     * @return UUID序列
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 实现js的escape函数
     *
     * @param input 待传入字符串
     * @return
     */
    public static String escape(String input) {
        int len = input.length(), i, j;
        StringBuilder result = new StringBuilder();
        result.ensureCapacity(len * 6);
        for (i = 0; i < len; i++) {
            j = input.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j)) {
                result.append(j);
            } else if (j < 256) {
                result.append("%");
                if (j < 16) {
                    result.append("0");
                }
                result.append(Integer.toString(j, 16));
            } else {
                result.append("%u").append(Integer.toString(j, 16));
            }
        }
        return result.toString();

    }

    /**
     * 实现js的unescape函数
     *
     * @param input 待传入字符串
     * @return
     */
    public static String unescape(String input) {
        int len = input.length();
        StringBuilder result = new StringBuilder();
        result.ensureCapacity(len);
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < len) {
            pos = input.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (input.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(input.substring(pos + 2, pos + 6), 16);
                    result.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(input.substring(pos + 1, pos + 3), 16);
                    result.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    result.append(input.substring(lastPos));
                    lastPos = len;
                } else {
                    result.append(input.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return result.toString();
    }

    /**
     * unicode转中文
     *
     * @param input 待传入字符串
     * @return 中文字符串
     */
    public static String toGb2312(String input) {
        return unescape(input.trim().replaceAll("(?i)\\\\u", "%u"));
    }

    /**
     * 中文字符串转unicode
     *
     * @param input 待传入字符串
     * @return unicode字符串
     */
    public static String toUnicode(String input) {
        input = input.trim();
        String output = escape(input).toLowerCase().replace("%u", "\\u");
        return output.replaceAll("(?i)%7b", "{")
                .replaceAll("(?i)%7d", "}")
                .replaceAll("(?i)%3a", ":")
                .replaceAll("(?i)%2c", ",")
                .replaceAll("(?i)%27", "'")
                .replaceAll("(?i)%22", "\"")
                .replaceAll("(?i)%5b", "[")
                .replaceAll("(?i)%5d", "]")
                .replaceAll("(?i)%3D", "=")
                .replaceAll("(?i)%20", " ")
                .replaceAll("(?i)%3E", ">")
                .replaceAll("(?i)%3C", "<")
                .replaceAll("(?i)%3F", "?")
                .replaceAll("(?i)%5c", "\\");
    }


    public static void main(String[] args) {
        String s = "Temperature: 98.6°F (37.0°C)爱淘";
        System.out.println(isBlank(new StringBuilder(" ait ao ")));
        System.out.println(isBlank(new StringBuilder("")));

        System.out.println(toUnicode("Temperature"));
        System.out.println(toUnicode("你好"));
        System.out.println(toUnicode(s));
        System.out.println(toGb2312("\u4f60\u597d"));
    }
}
