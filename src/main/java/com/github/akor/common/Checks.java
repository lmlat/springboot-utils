package com.github.akor.common;

import com.github.akor.enums.Regex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author : AiTao
 * Create : 2021/4/12 17:53
 * Description : 校验工具类
 */
public class Checks {
    private static final Logger LOGGER = LoggerFactory.getLogger(Checks.class);
    //空串
    private static final String EMPTY = "";

    private Checks() {
        throw new RuntimeException("拒绝反射创建对象");
    }

    /**
     * 正则校验
     *
     * @param regex   正则表达式
     * @param content 校验内容
     * @return true匹配, false不匹配
     */
    public static boolean isMatch(String regex, String content) {
        return Pattern.matches(regex, content);
    }

    /**
     * 正则匹配（判空检验）
     *
     * @param regex   正则表达式
     * @param content 校验内容
     */
    private static boolean matcher(String regex, String content) {
        return isNotBlank(content) && isMatch(regex, trim(content));
    }

    public static Matcher newMatcher(String regex, String content, int flags) {
        return Pattern.compile(regex, flags).matcher(content);
    }

    /**
     * 校验空对象
     *
     * @param obj 对象
     * @return {@link Boolean true表示对象为空}
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        //处理字符串类型
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        //处理集合类型
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        //处理Map类型
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        //处理数组类型
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        return false;
    }

    public static boolean isEmpty(CharSequence... array) {
        return isAny(array, false);
    }

    @SafeVarargs
    public static <T> boolean isEmpty(T[]... arrays) {
        for (T[] array : arrays) {
            if (!isEmpty(array)) {
                return false;
            }
        }
        return true;
    }

    @SafeVarargs
    public static <T> boolean isEmpty(Collection<T>... cs) {
        for (Collection<T> c : cs) {
            if (!isEmpty(c)) {
                return false;
            }
        }
        return true;
    }

    @SafeVarargs
    public static <K, V> boolean isEmpty(Map<K, V>... maps) {
        for (Map<K, V> map : maps) {
            if (!isEmpty(map)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 校验非空对象
     *
     * @param str 对象
     * @return {@link Boolean true表示对象非空}
     */
    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static boolean isNotEmpty(CharSequence... array) {
        return !isEmpty(array);
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    @SafeVarargs
    public static <T> boolean isNotEmpty(T[]... arrays) {
        return !isEmpty(arrays);
    }

    public static <T> boolean isNotEmpty(Collection<T> c) {
        return !isEmpty(c);
    }

    @SafeVarargs
    public static <T> boolean isNotEmpty(Collection<T>... cs) {
        return !isEmpty(cs);
    }

    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return !isEmpty(map);
    }

    @SafeVarargs
    public static <K, V> boolean isNotEmpty(Map<K, V>... maps) {
        return !isEmpty(maps);
    }

    /**
     * <pre>
     *     isBlank("   ")           = true
     *     isBlank("")              = true
     *     isBlank(" adf df ")      = false
     *     isBlank("a    b")        = false
     * </pre>
     */
    public static boolean isBlank(CharSequence str) {
        return isEmpty(str) || isAllTrim(str.toString());
    }

    public static boolean isBlank(CharSequence... array) {
        return isAny(array, true);
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    public static boolean isNoteBlank(CharSequence... array) {
        return !isBlank(array);
    }

    /**
     * 判空校验
     *
     * @param array 数据集
     * @param flag  true isBlank, false isEmpty
     * @return 给定数据集中，任意一个数组项为空，返回true，否则返回false
     */
    private static boolean isAny(CharSequence[] array, boolean flag) {
        for (CharSequence str : array) {
            if (!flag && isEmpty(str) || flag && isBlank(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 匹配首部留白内容
     *
     * @param str 内容
     */
    public static boolean isLeftTrim(String str) {
        return isMatch(Regex.RG_LEFT_TRIM_VERIFY.getValue(), str);
    }

    /**
     * 匹配尾部留白内容
     *
     * @param str 内容
     */
    public static boolean isRightTrim(String str) {
        return isMatch(Regex.RG_RIGHT_TRIM_VERIFY.getValue(), str);
    }

    /**
     * 匹配首尾部留白内容
     *
     * @param str 内容
     */
    public static boolean isTrim(String str) {
        return isLeftTrim(str) && isRightTrim(str);
    }

    /**
     * 检测内容是否为空串
     * <pre>
     *     isAllTrim("")        = true
     *     isAllTrim(" ")       = true
     *     isAllTrim("   ")     = true
     *     isAllTrim("  a  ")   = false
     *     isAllTrim(null)      = true
     * </pre>
     *
     * @param str 字符串
     * @return true or false
     */
    public static boolean isAllTrim(String str) {
        if (isEmpty(str)) {
            return true;
        }
        return isMatch(Regex.RG_ALL_TRIM.getValue(), str);
    }

    /**
     * 清除左留白
     *
     * @param str 字符串
     */
    public static String trimLeft(String str) {
        return str.replaceAll(Regex.RG_LEFT_TRIM.getValue(), EMPTY);
    }

    /**
     * 清除右留白
     *
     * @param str 字符串
     */
    public static String trimRight(String str) {
        return str.replaceAll(Regex.RG_RIGHT_TRIM.getValue(), EMPTY);
    }

    /**
     * 清除左右留白
     *
     * @param str 字符串
     */
    public static String trim(String str) {
        return str.replaceAll(Regex.RG_LEFT_RIGHT_TRIM.getValue(), EMPTY);
    }

    /**
     * 清除所有留白(包含中间内容)
     *
     * @param str 字符串
     */
    public static String trimAll(String str) {
        return str.replaceAll(Regex.RG_ALL_TRIM.getValue(), EMPTY);
    }

    /**
     * 校验手机号
     *
     * @param phone 手机号
     */
    public static boolean isPhone(String phone) {
        return isNotBlank(phone) && !trim(phone).equals("") && trim(phone).length() == 11 && isMatch(Regex.RG_PHONE.getValue(), phone);
    }

    /**
     * 校验邮箱
     *
     * @param email 邮箱
     */
    public static boolean isEmail(String email) {
        return matcher(Regex.RG_EMAIL.getValue(), trim(email));
    }

    /**
     * 校验用户名
     *
     * @param username 用户名
     */
    public static boolean isUsername(String username) {
        return matcher(Regex.RG_USERNAME.getValue(), trim(username));
    }

    /**
     * 校验密码
     *
     * @param password 密码
     */
    public static boolean isPassword(String password) {
        return matcher(Regex.RG_PASSWORD.getValue(), trim(password));
    }

    /**
     * 校验密码
     *
     * @param password 密码
     */
    public static boolean isStrongPassword(String password) {
        return matcher(Regex.RG_PASSWORD_STRONG.getValue(), trim(password));
    }

    /**
     * 校验验证码
     *
     * @param code 验证码
     */
    public static boolean isValidCode(String code) {
        return matcher(Regex.RG_VALID_CODE.getValue(), trim(code));
    }

    /**
     * 校验ipv4地址
     *
     * @param ipv4 IP地址
     */
    public static boolean isIP(String ipv4) {
        return matcher(Regex.RG_IPV4.getValue(), trim(ipv4));
    }

    /**
     * 校验图片地址
     *
     * @param imageUrl 图片URL
     */
    public static boolean isImage(String imageUrl) {
        return matcher(Regex.RG_IMG.getValue(), trim(imageUrl));
    }

    /**
     * 校验日期地址
     *
     * @param date 日期值
     */
    public static boolean isDate(String date) {
        return matcher(Regex.RG_DATE.getValue(), trim(date));
    }

    /**
     * 校验车牌号
     *
     * @param carNumber 车牌号
     */
    public static boolean isCarNumber(String carNumber) {
        return matcher(Regex.RG_CAR_NUMBER.getValue(), trim(carNumber));
    }

    /**
     * 校验汉字
     *
     * @param chinese 汉字
     */
    public static boolean isChinese(String chinese) {
        return matcher(Regex.RG_CHINESE.getValue(), trim(chinese));
    }

    /**
     * 校验数字
     *
     * @param digit 数字
     */
    public static boolean isDigit(String digit) {
        return matcher(Regex.RG_NUMBER.getValue(), trim(digit));
    }

    /**
     * 校验字母
     *
     * @param letter 字母
     */
    public static boolean isLetter(String letter) {
        return matcher(Regex.RG_LETTER.getValue(), trim(letter));
    }

    /**
     * 校验HTTP地址
     *
     * @param url 地址
     */
    public static boolean isUrl(String url) {
        return matcher(Regex.RG_URL.getValue(), trim(url));
    }

    public static boolean isVersion(String version) {
        return matcher(Regex.RG_VERSION_NUMBER.getValue(), version);
    }

    /**
     * 实现replaceFirst方法
     *
     * @param oldStr 旧字符串
     * @param newStr 新字符串
     * @return 返回替换后的字符串
     */
    public static String replaceFirst(String oldStr, String newStr) {
        StringBuffer res = new StringBuffer();
        Matcher matcher = newMatcher(oldStr, newStr, 0);
        while (matcher.find()) {
            matcher.appendReplacement(res, newStr);
            matcher.appendTail(res);
        }
        return res.toString();
    }

    /**
     * 实现replaceAll方法
     *
     * @param oldStr 旧字符串
     * @param newStr 新字符串
     * @return 返回替换后的字符串
     */
    public static String replaceAll(String oldStr, String newStr) {
        StringBuffer res = new StringBuffer();
        Matcher matcher = newMatcher(oldStr, newStr, 0);
        boolean flag = false;
        while (matcher.find()) {
            matcher.appendReplacement(res, newStr);
            flag = true;
        }
        if (flag) {
            res = matcher.appendTail(res);
        }
        return res.toString();
    }

    /**
     * 内容去重
     *
     * @param content 内容
     * @return 返回去重后的内容
     */
    public static String distinct(String content) {
        return newMatcher(Regex.RG_REMOVE_DUPLICATE.getValue(), content, 0).replaceAll("$1");
    }

    /**
     * 获取img标签中的src属性值
     *
     * @param content 搜索内容
     * @return Map<Integer, String> key:序号 value:图片地址
     */
    public static Map<Integer, String> getImgStr(String content) {
        Map<Integer, String> picMap = new HashMap<>();
        int count = 0;
        Matcher matcher = newMatcher("<img.*src\\s*=\\s*(.*?)[^>]*?>", content, Pattern.CASE_INSENSITIVE);
        while (matcher.find()) {
            String img = matcher.group();// 得到<img />数据
            System.out.println("img:" + img);
            Matcher m = newMatcher("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)", img, 0);// 匹配<img>中的src数据
            while (m.find()) {
                picMap.put(++count, m.group(1));
            }
        }
        return picMap;
    }

    public static Map<String, String> getVideoStr(String htmlStr) {
        Map<String, String> pics = new HashMap<String, String>();
        String regEx_video = "<video.*poster\\s*=\\s*(.*?)[^>]*?src\\s*=\\s*(.*?)[^>]*?>";
        Pattern p = Pattern.compile(regEx_video, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlStr);
        String video = "";
        Map<String, String> map = new HashMap<String, String>();
        while (m.find()) {
            video = m.group();
            Matcher mPoster = newMatcher("poster\\s*=\\s*\"?(.*?)(\"|>|\\s+)", video, 0);
            Matcher mSrc = newMatcher("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)", video, 0);
            String poster = "";
            String src = "";
            while (mPoster.find()) {
                poster = mPoster.group(1);
            }
            while (mSrc.find()) {
                src = mSrc.group(1);
            }
            map.put("poster", poster);
            map.put("src", src);
        }
        return map;
    }

    /**
     * 替换一段内容中指定标签的属性值
     *
     * @param str      内容
     * @param tag      标签名
     * @param tagAttr  标签中的某个属性名
     * @param tagValue 替换后的标签值
     * @param isAppend 是否追加原始值
     */
    public static String replaceTagPropertyValue(String str, String tag, String tagAttr, String tagValue, boolean isAppend) {
        String rgForTag = String.format("<\\s*%s\\s+([^>]*)\\s*", tag);
        String rgForTagAttrib = String.format("%s=\\s*\"([^\"]+)\"", tagAttr);
        Pattern patternForTag = Pattern.compile(rgForTag, Pattern.CASE_INSENSITIVE);
        Pattern patternForAttrib = Pattern.compile(rgForTagAttrib, Pattern.CASE_INSENSITIVE);
        Matcher matcherForTag = patternForTag.matcher(str);
        String prefix = tagAttr + "=\"";//前缀
        String suffix = "\"";//后缀
        StringBuffer result = new StringBuffer();
        while (matcherForTag.find()) {
            StringBuffer afterTag = new StringBuffer("<" + tag + " ");
            String oldPropertyValue = matcherForTag.group(1);//原始属性值
            Matcher matcherForAttr = patternForAttrib.matcher(oldPropertyValue);
            if (matcherForAttr.find()) {
                tagValue = prefix + tagValue + (isAppend ? matcherForAttr.group(1) : "") + suffix;
                matcherForAttr.appendReplacement(afterTag, tagValue);
            }
            matcherForAttr.appendTail(afterTag);
            matcherForTag.appendReplacement(result, afterTag.toString());
        }
        matcherForTag.appendTail(result);
        return result.toString();
    }

    /**
     * 匹配字符串是否存在指定内容
     *
     * @param regex   表达示
     * @param content 内容
     */
    public static boolean contain(String regex, String content) {
        return newMatcher(regex, content, 0).find();
    }

    /**
     * 匹配字符串是否存在指定前缀
     *
     * @param content 匹配内容
     * @param prefix  前缀
     */
    public static boolean startsWith(String content, String prefix) {
        return isMatch(String.format("\\b%s\\w*\\b", prefix.trim()), content.trim());
    }

    /**
     * 匹配字符串是否存在指定后缀
     *
     * @param content 匹配内容
     * @param suffix  后缀
     */
    public static boolean endsWith(String content, String suffix) {
        return isMatch(String.format("\\b\\w*%s\\b", suffix), content);
    }

    public static void main(String[] args) {
//        StringBuffer content = new StringBuffer();
//        content.append("<ul class=\"imgBox\"><li><img id=\"160424\" src=\"uploads/allimg/160424/1-160424120T1-50.jpg\" class=\"src_class\"></li>");
//        content.append("<li><img id=\"150628\" src=\"uploads/allimg/150628/1-15062Q12247.jpg\" class=\"src_class\"></li></ul>");
//        System.out.println(getImgStr(content.toString()));
//        System.out.println("原始字符串为:" + content.toString());
//        String newStr = replaceTagPropertyValue(content.toString(), "ul", "class", "ul-", true);
//        System.out.println("替换后为:" + newStr);
//        System.out.println(isVersion("101.101.11"));
        System.out.println(distinct("aaaabbbbbcccccdddddeeeeeffffff艾艾艾艾"));
        System.out.println(replaceAll("aaabbbdddfff", ""));
//        System.out.println(trimLeft("    b   c   "));
//        System.out.println(isLeftTrim("b   c   "));
//        System.out.println(isRightTrim("    b   c"));
//        System.out.println(isAllTrim("                    a   "));
//        System.out.println(isTrim("    b   c   "));
//        System.out.println(trimRight("b   c"));
//        System.out.println(trim("    b   c   "));
//        System.out.println(trimAll("b c"));
//        System.out.println(isBlank("             c"));
        System.out.println(Checks.contain("[\\u2E80-\\u9FFF]", "aitao啊去妇女和v"));//内容是否包含汉字
        System.out.println(isAllTrim(null));
        Pattern pattern = Pattern.compile("(\\d{3})(-)(\\d{3})(-)(\\d{4})");
        Matcher matcher = pattern.matcher("313-555-1234");
        while (matcher.find()) {
            System.out.println(matcher.group(1));//313
            System.out.println(matcher.group(3));//555
            System.out.println(matcher.group(5));//1234
        }
        System.out.println(contain("(\\d{3})(-)(\\d{3})(-)(\\d{4})", "313-555-1234"));
    }
}
