package com.github.akor.util;

import com.github.akor.common.Checks;
import com.github.akor.common.StringUtils;
import javafx.util.Pair;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author : AiTao
 * @Create : 2021-12-18 20:35
 * @Description : URL链接工具类
 */
public class UrlUtils {
    public UrlUtils() {
        throw new RuntimeException("拒绝反射实例化对象");
    }

    /**
     * 追加式向链接中添加参数
     *
     * @param url    链接地址
     * @param params 传递参数
     * @return 添加了指定参数后的URL地址
     */
    public static String append(String url, Map<String, String> params) {
        if (StringUtils.isBlank(url)) {
            return "";
        } else if (Checks.isEmpty(params)) {
            return url.trim();
        } else {
            StringBuilder buildParams = buildParams(params);
            System.out.println(buildParams.toString());
            StringBuilder buildUrl = new StringBuilder(url.trim());
            if (url.contains("?")) {
                if (buildUrl.charAt(buildUrl.length() - 1) == '?') {//case1：http://wwww.baidu.com?
                    buildUrl.append(buildParams);
                } else {//case2：http://wwww.baidu.com?aa=11
                    buildUrl.append("&").append(buildParams);
                }
            } else {//case3：http://wwww.baidu.com
                buildUrl.append("?").append(buildParams);
            }
            return buildUrl.toString();
        }
    }

    public static String append(String url, Pair<String, String> pair) {
        return append(url, pair.getKey(), pair.getValue());
    }

    public static String append(String url, String name, String value) {
        if (Checks.isBlank(url)) {
            return "";
        } else if (Checks.isBlank(name)) {
            return url.trim();
        } else {
            Map<String, String> params = new HashMap<>();
            params.put(name, value);
            return append(url, params);
        }
    }

    /**
     * 覆盖式向链接中添加参数
     *
     * @param url    链接地址
     * @param params 传递参数
     * @return 添加了指定参数后的URL地址
     */
    public static String add(String url, Map<String, String> params) {
        return append(clear(url), params);
    }

    public static String add(String url, Pair<String, String> pair) {
        return add(url, pair.getKey(), pair.getValue());
    }

    public static String add(String url, String name, String value) {
        return append(clear(url), name, value);
    }

    /**
     * 删除链接中的参数
     *
     * @param url   链接地址
     * @param names 参数名
     * @return 删除指定参数后的URL地址
     */
    public static String remove(String url, String... names) {
        if (Checks.isBlank(url)) {
            return "";
        } else if (Checks.isEmpty(names)) {
            return url.trim();
        } else {
            url = url.trim();
            int len = url.length();
            int idx = url.indexOf("?");
            if (idx > -1) {
                if (url.charAt(len - 1) == '?') {//case1：http://wwww.baidu.com?
                    return url;
                } else {//case2：http://wwww.baidu.com?aa=11或http://wwww.baidu.com?aa=或http://wwww.baidu.com?aa
                    //基本链接地址，不含参数部分
                    StringBuilder baseUrl = new StringBuilder(url.substring(0, idx));
                    String[] paramsArray = url.substring(idx + 1).split("&");
                    if (Checks.isNotEmpty(paramsArray)) {
                        Map<String, String> paramsMap = new HashMap<>();
                        for (String param : paramsArray) {
                            if (!Checks.isBlank(param)) {
                                String[] pair = param.split("=");//0：参数名，1：参数值
                                int i = 0;
                                for (; i < names.length; i++) {
                                    if (names[i].equals(pair[0])) {
                                        break;
                                    }
                                }
                                if (i == names.length) {
                                    paramsMap.put(pair[0], (pair.length > 1) ? pair[1] : "");
                                }
                            }
                        }
                        return Checks.isNotEmpty(paramsMap) ? buildParams(baseUrl, paramsMap).toString() : baseUrl.toString();
                    }
                }
            }
            return url;
        }
    }

    public static String remove(String url, Collection<String> c) {
        return remove(url, c.toArray(new String[c.size()]));
    }

    public static String clear(String url) {
        int idx = url.indexOf("?");
        return idx > -1 ? url.substring(0, idx) : url;
    }

    /**
     * URL编码
     *
     * @param url     url地址
     * @param charset 编码方式
     * @return 编码后的URL
     */
    public static String encode(String url, String charset) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        try {
            StringBuilder buildUrl = new StringBuilder();
            String[] urlSplit = url.split("\\?");
            if (urlSplit.length > 1) {
                urlSplit[1] = URLDecoder.decode(urlSplit[1], charset);
            }
            return buildUrl.append(urlSplit[0]).append("?").append(urlSplit[1]).toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String encode(String url, Charset charset) {
        return encode(url, charset.name());
    }

    public static String encode(String url) {
        return encode(url, StandardCharsets.UTF_8);
    }


    /**
     * url解码
     *
     * @param url     url地址
     * @param charset 编码方式
     * @return 编码后的URL地址
     */
    public static String decode(String url, String charset) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        try {
            return URLDecoder.decode(url, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String decode(String url, Charset charset) {
        return decode(url, charset.name());
    }

    public static String decode(String url) {
        return decode(url, StandardCharsets.UTF_8);
    }

    /**
     * 构建传递参数体
     *
     * @param params 参数体
     * @return 返回
     * @see StringBuilder
     */
    private static StringBuilder buildParams(Map<String, String> params) {
        return buildParams("", params);
    }

    private static StringBuilder buildParams(CharSequence url, Map<String, String> params) {
        StringBuilder buildParams = new StringBuilder(url);
        System.out.println(buildParams);
        if (Checks.isNotEmpty(buildParams) && buildParams.charAt(buildParams.length() - 1) != '?') {
            buildParams.append("?");
        }
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            buildParams.append(key).append("=").append(params.get(key));
            if (iterator.hasNext()) {
                buildParams.append("&");
            }
        }
        return buildParams;
    }


    public static void main(String[] args) {
        String a = "http://wwww.baidu.com";
        String b = "http://wwww.baidu.com?";
        String c = "http://wwww.baidu.com?aa=11";
        System.out.println("a=" + append(a, "bb", "1"));
        System.out.println("b=" + append(b, "bb", "1"));
        System.out.println("c=" + append(c, "bb", "1"));

        System.out.println("a=" + add(a, "bb", "1"));
        System.out.println("b=" + add(b, "bb", "1"));
        System.out.println("c=" + add(c, "bb", "1"));

        String d = "http://wwww.baidu.com?aa";
        String e = "http://wwww.baidu.com?aa=11&bb=22&cc=33";
        String f = "http://wwww.baidu.com?aa=11&bb=22&cc=33&dd=";
        String g = "http://wwww.baidu.com?aa=11&bb=22&cc=33&dd";
        System.out.println("g=" + remove(g, "cc", "aa"));

        String h = "https://www.baidu.com?message=我爱你";
        String hh = encode(h);
        System.out.println(hh);
        System.out.println(decode(hh));
        System.out.println("h=" + clear(h));
        System.out.println("h=" + append(h, "name", "艾韬"));
    }
}
