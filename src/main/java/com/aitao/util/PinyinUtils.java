package com.aitao.util;


import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * @Author : AiTao
 * @Create : 2021-12-18 20:35
 * @Description : 拼音工具类
 */
public class PinyinUtils {

    /**
     * 解析汉字拼音首字母缩写, 例如，艾韬 =》 AT
     * 暂时解决不了多音字的问题，只能使用取多音字的第一个音的方案
     *
     * @param chinese 汉字
     * @return 大写汉子首字母; 如果都转换失败,那么返回null
     */
    public static String parse(String chinese) {
        String result = null;
        if (chinese != null && !chinese.isEmpty()) {
            char[] charArray = chinese.toCharArray();
            StringBuilder sb = new StringBuilder();
            for (char ch : charArray) {
                // 逐个汉字进行转换， 每个汉字返回值为一个String数组（因为有多音字）
                String[] stringArray = PinyinHelper.toHanyuPinyinStringArray(ch);
                if (stringArray != null) {
                    sb.append(stringArray[0].charAt(0));
                }
            }
            if (sb.length() > 0) {
                result = sb.toString().toUpperCase();
            }
        }
        return result;
    }


    /**
     * 获取汉字拼音的方法。如： 张三 --> zhangsan
     * <p>
     * <p>
     * 说明：暂时解决不了多音字的问题，只能使用取多音字的第一个音的方案
     *
     * @param hanzi 汉子字符串
     * @return 汉字拼音; 如果都转换失败,那么返回null
     */
    public static String getPinyin(String hanzi) {
        String result = null;
        if (null != hanzi && !"".equals(hanzi)) {
            char[] charArray = hanzi.toCharArray();
            StringBuffer sb = new StringBuffer();
            for (char ch : charArray) {
                // 逐个汉字进行转换， 每个汉字返回值为一个String数组（因为有多音字）
                String[] stringArray = PinyinHelper.toHanyuPinyinStringArray(ch);
                if (null != stringArray) {
                    // 把第几声这个数字给去掉
                    sb.append(stringArray[0].replaceAll("\\d", ""));
                }
            }
            if (sb.length() > 0) {
                result = sb.toString();
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(PinyinUtils.parse("袁素芳"));
        System.out.println(PinyinUtils.parse("艾韬"));
        System.out.println(PinyinUtils.parse("袁素芳"));
    }
}
