package com.github.akor.enums;

/**
 * Author : AiTao
 * Create : 2021/3/13 5:45
 * Description : 常量类
 */
public final class Const {
    //==========HTTP状态===================================
    /**
     * 操作成功
     */
    public static final int SUCCESS = 200;

    /**
     * 对象创建成功
     */
    public static final int CREATED = 201;

    /**
     * 请求已经被接受
     */
    public static final int ACCEPTED = 202;

    /**
     * 操作已经执行成功，但是没有返回数据
     */
    public static final int NO_CONTENT = 204;

    /**
     * 资源已被移除
     */
    public static final int MOVED_PERM = 301;

    /**
     * 重定向
     */
    public static final int SEE_OTHER = 303;

    /**
     * 资源没有被修改
     */
    public static final int NOT_MODIFIED = 304;

    /**
     * 参数列表错误（缺少，格式不匹配）
     */
    public static final int BAD_REQUEST = 400;

    /**
     * 未授权
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 访问受限，授权过期
     */
    public static final int FORBIDDEN = 403;

    /**
     * 资源，服务未找到
     */
    public static final int NOT_FOUND = 404;

    /**
     * 不允许的http方法
     */
    public static final int BAD_METHOD = 405;
    /**
     * 请求超时
     */
    public static final int TIME_OUT = 408;

    /**
     * 资源冲突，或者资源被锁
     */
    public static final int CONFLICT = 409;

    /**
     * 不支持的数据，媒体类型
     */
    public static final int UNSUPPORTED_TYPE = 415;

    /**
     * 系统内部错误
     */
    public static final int ERROR = 500;

    /**
     * 接口未实现
     */
    public static final int NOT_IMPLEMENTED = 501;


    //==========正则校验===================================
    /**
     * 获取IP地址 http://127.0.0.1:8080
     */
    public static final String IP_ADDRESS = "((http|ftp|https)://)(([a-zA-Z0-9._-]+)|([0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}))(([a-zA-Z]{2,6})|(:[0-9]{1,4})?)";
    /**
     * 清除首尾空白字符
     */
    public static final String REGEXP_TRIM = "(^\\s*)|(\\s*$)";
    /**
     * 邮箱
     */
    public static final String REGEXP_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
    /**
     * 手机号
     */
    public static final String REGEXP_PHONE = "^13[0-9]{9}|15[012356789][0-9]{8}|18[0-9]{9}|(14[57][0-9]{8})|(17[015678][0-9]{8})$";
    /**
     * 账户名
     */
    public static final String REGEXP_USERNAME = "^[a-zA-Z\\u2E80-\\u9FFF]{2,20}$";
    /**
     * 账户密码,以字母开头
     */
    public static final String REGEXP_PASSWORD = "^[a-zA-z0-9.]\\w{6,16}$";
    /**
     * 强密码，必须包含大小写字母和数字的组合，不能使用特殊字符，长度在6-16之间
     */
    public static final String REGEXP_PASSWORD_STRONG = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,16}$";
    /**
     * 汉字
     */
    public static final String REGEXP_CHINESE = "^[\\u2E80-\\u9FFF]+$";
    /**
     * 验证码
     */
    public static final String REGEXP_VALID_CODE = "^[a-zA-Z0-9]{5}$";
    /**
     * 正负数，符号位(0个或1个) + 数字(1个或多个) + 小数点(0个或1个) + 数字(1个或多个)
     */
    public static final String REGEXP_NUMBER = "^([+-]?)\\d{0,}\\.?\\d{1,}$";
    /**
     * 正整数
     */
    public static final String REGEXP_POSITIVE_INTEGER = "^[1-9]\\d{0,}$";
    /**
     * 负整数
     */
    public static final String REGEXP_NEGTIVE_INTEGER = "^([-]?)[1-9]\\d{0,}$";
    /**
     * 浮点数
     */
    public static final String REGEXP_FLOAT_NUMBER = "^([+-]?)\\d{0,}\\.?\\d{1,}$";
    /**
     * 字母
     */
    public static final String REGEXP_LETTER = "^[a-zA-Z]{1,}$";
    /**
     * 大写字母
     */
    public static final String REGEXP_UPPERCASE_LETTER = "^[A-Z]{1,}$";
    /**
     * 小写字母
     */
    public static final String REGEXP_LOWERCASE_LETTER = "^[a-z]{1,}$";
    /**
     * 邮政编码
     */
    public static final String REGEXP_POSTAL_CODE = "^\\d{6}$";
    /**
     * IP v4地址
     */
    public static final String REGEXP_IPV4 = "^(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})){3}$";
    /**
     * 图片地址 *.png
     */
    public static final String REGEXP_IMG = "(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$";
    /**
     * 日期(yyyy-MM-dd)|(yyyy/MM/dd)|(yyyy年MM月dd日)
     */
    public static final String REGEXP_DATE = "^\\d{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$";
    /**
     * 压缩文件
     */
    public static final String REGEXP_RAR_FILE = "(.*)\\.(rar|zip|7zip|tgz)$";
    /**
     * 车牌号正则
     */
    public static final String REGEXP_CAR_NUMBER = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";
    /**
     * QQ号码，最短5位，最长15位数字
     */
    public static final String REGEXP_QQ_NUMBER = "^[1-9]\\d{4,14}$";
    /**
     * URL地址： 包括 http, ftp等
     */
    public static final String REGEX_URL = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/[^/][\\u2E80-\\u9FFFa-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&amp;%\\$#\\=~_\\-@]*)*$";
}
