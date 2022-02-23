package com.github.akor.files;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/07/15 9:55
 * @Description : fastjson工具类
 */
public class JsonUtils {
    //日志对象
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);
    //字符串遍历迭代器
    private static CharacterIterator iterator;
    //记录当前迭代字符
    private static char currentChar;
    //记录当前迭代索引列
    private static int currentCol;
    //序列化配置
    private static final SerializeConfig SERIALIZE_CONFIG;
    // 不同类型空值转换格式
    private static final SerializerFeature[] SERIALIZER_FEATURES = {
            //输出空值字段
            SerializerFeature.WriteMapNullValue,
            //数组结果为null,则输出为[]
            SerializerFeature.WriteNullListAsEmpty,
            //数值字段为null,则输出为0
            SerializerFeature.WriteNullNumberAsZero,
            //boolean字段为null,则输出为false
            SerializerFeature.WriteNullBooleanAsFalse,
            //字符类型为null,则输出为空串""
            SerializerFeature.WriteNullStringAsEmpty,
            //打开循环引用检测，JSONField(serialize = false)不循环
            SerializerFeature.DisableCircularReferenceDetect,
            //默认使用系统日期格式
            SerializerFeature.WriteDateUseDateFormat
    };
    private static final Character ARRAY_PREFIX = '[';
    private static final Character ARRAY_SUFFIX = ']';
    private static final Character OBJECT_PREFIX = '{';
    private static final Character OBJECT_SUFFIX = '}';
    private static final Character QUOTATION_MARKS = '"';
    private static final Character ESCAPE_CHAR_PREFIX = '\\';
    private static final Character POSITIVE_SIGN = '+';
    private static final Character NEGTIVE_SIGN = '-';
    //转义字符
    private static final String ESCAPE_CHARS = " \\\"/bfnrtu";
    //十六进制字符表
    private static final String HEX_CHARS = "0123456789abcdefABCDEF";

    static {
        SERIALIZE_CONFIG = new SerializeConfig();
        // 兼容日期输出格式，默认的日期是返回时间戳
        SERIALIZE_CONFIG.put(Date.class, new JSONLibDataFormatSerializer());
        SERIALIZE_CONFIG.put(java.sql.Date.class, new JSONLibDataFormatSerializer());
    }

    public static void main(String[] args) {
//        JSONArray jsonObject = newJsons(load("C:\\Users\\10034\\Desktop\\Learn\\springboot-utils\\target\\classes\\data1.json"));
//        insertSuffixChars(load("C:\\Users\\10034\\Desktop\\Learn\\springboot-utils\\target\\classes\\data1.json"));
//        System.out.println(jsonObject.toJSONString());
//        System.out.println(newJsons(""));
        System.out.println(isValid("[{\"data\": \"2017-04-21\", age: 14}]"));
        System.out.println(isValid("{\"data\": \"2017-04-21\", \"age\": \"true\"}"));
        System.out.println(isValid("{\"data\": \"2017-04-21\", \"age\": \"\t\n\"}"));
        System.out.println(isValid("{\"data\": \"2017-04-21\", \"age\": 14}"));
        System.out.println(isValid("{\"data\": \"2017-04-21\", \"age\": -999}"));
        System.out.println(isValid("{\"data\": \"2017-04-21\", \"age\": +6666}"));
        System.out.println(isValid("{\"data\": \"2017-04-21\", \"age\": \"null\"}"));
        System.out.println(isValid("{\"age\": 011111}"));
        System.out.println(isJson("[{\"age\": 011111}]"));
        JSONArray jsonObject = deSerialize("{\"age\": \"undefined\"}", JSONArray.class);
        System.out.println(jsonObject);
    }

    /**
     * 加载json类型文件
     *
     * @param path 文件路径
     * @return json格式串
     */
    public static String load(String path) {
        return load(new File(path));
    }

    public static String load(File jsonFile) {
        return load(jsonFile, "UTF-8");
    }

    public static String load(File jsonFile, String charset) {
        if (jsonFile == null) {
            throw new NullPointerException();
        }
        if (!jsonFile.exists()) {
            LOGGER.error("json文件不存在, path:{}", jsonFile.getAbsolutePath());
            return null;
        }
        if (jsonFile.isDirectory()) {
            LOGGER.error("非json文件类型");
            return null;
        }
        StringBuilder jsonString = new StringBuilder();
        try (Reader reader = new InputStreamReader(new FileInputStream(jsonFile), charset)) {
            //读取文件内容
            int ch;
            while ((ch = reader.read()) != -1) {
                jsonString.append((char) ch);
            }
        } catch (IOException e) {
            LOGGER.error("json文件加载异常", e);
        }
        return jsonString.toString();
    }

    /**
     * 是否满足json串格式
     *
     * @param jsonString json串
     * @return true or false
     */
    public static boolean isJson(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return false;
        }
        boolean isJsonObject = true, isJsonArray = true;
        try {
            JSONObject.parseObject(jsonString);
        } catch (Exception e) {
            isJsonObject = false;
        }
        if (isJsonObject) {
            return true;
        }
        try {
            JSONObject.parseArray(jsonString);
        } catch (Exception e) {
            isJsonArray = false;
        }
        return isJsonArray;
    }

    /**
     * 实例化 JSONObject
     */
    public static JSONObject newJson() {
        return new JSONObject();
    }

    public static JSONObject newJson(Object object) {
        return newJson(toMap(object));
    }

    public static JSONObject newJson(String jsonString) {
        return newJson(toMap(jsonString));
    }

    public static JSONObject newJson(Map<String, Object> map) {
        return new JSONObject(map == null || map.isEmpty() ? new HashMap<>() : map);
    }

    /**
     * 实例化 JSONArray
     */
    public static JSONArray newJsons() {
        return new JSONArray();
    }

    public static JSONArray newJsons(String jsonString) {
        return newJsons(toList(jsonString));
    }

    public static JSONArray newJsons(List<Object> list) {
        return new JSONArray(list == null || list.isEmpty() ? new ArrayList<>() : list);
    }

    public static JSONArray newJsons(Object... objects) {
        if (objects == null || objects.length == 0) {
            return newJsons();
        }
        List<Object> list = new ArrayList<>();
        Collections.addAll(list, objects);
        return newJsons(list);
    }

    /**
     * 序列化对象
     *
     * @param obj 对象
     * @param <T> 任意类型
     * @return json串
     */
    public static <T> String toSerialize(T obj) {
        return toString(obj);
    }

    /**
     * 反序列化
     *
     * @param jsonString json串
     * @param clazz      反序列化类型
     * @param <T>        任意类型
     * @return 对象
     */
    public static <T> T deSerialize(String jsonString, Class<T> clazz) {
        return toBean(jsonString, clazz);
    }

    /**
     * 反序列化
     *
     * @param jsonString    json串
     * @param typeReference 类型引用
     * @param <T>           任意类型
     * @return 对象
     */
    public static <T> T deSerialize(String jsonString, TypeReference<T> typeReference) {
        return toBean(jsonString, typeReference);
    }

    /**
     * List<Person>、Person转换成Json串
     * 日期类型转换结果：{"date":16,"hours":14,"seconds":38,"month":6,"timezoneOffset":-480,"year":121,"minutes":17,"time":1626416258035,"day":5}
     *
     * @param obj 对象实例
     * @param <T> 任意类型
     * @return 返回一个JSON格式的字符串
     */
    public static <T> String toString(T obj) {
        try {
            return JSONObject.toJSONString(obj, SERIALIZE_CONFIG, SERIALIZER_FEATURES);
        } catch (Exception e) {
            LOGGER.error("转换json格式串失败", e);
        }
        return null;
    }

    /**
     * 将Json字符串转换为Object类型
     *
     * @param jsonString json格式串
     * @return Object
     */
    public static Object toObject(String jsonString) {
        return toBean(jsonString, new TypeReference<Object>() {
        });
    }


    /**
     * 将json字符串转为List集合
     *
     * @param jsonString json格式串
     * @param <T>        泛型
     * @return 返回转换后的对象集合
     */
    public static <T> List<T> toList(String jsonString) {
        List<T> list = new ArrayList<>();
        try {
            list = toBean(jsonString, new TypeReference<List<T>>() {
            });
        } catch (Exception e) {
            LOGGER.error("json格式串转换List失败: jsonString:{}", jsonString, e);
        }
        return list;
    }

    /**
     * 将json字符串转为Set集合
     *
     * @param jsonString json格式串
     * @param <T>        泛型
     * @return 返回转换后的对象集合
     */
    public static <T> Set<T> toSet(String jsonString) {
        try {
            List<T> objs = toList(jsonString);
            return Objects.nonNull(objs) ? new HashSet<>(objs) : null;
        } catch (Exception e) {
            LOGGER.error("json格式串转换Set失败: jsonString:{}", jsonString, e);
        }
        return null;
    }

    /**
     * 将Json字符串转换为Map<String,T>类型
     *
     * @param jsonString json格式串
     * @param <T>        任意类型
     * @return Map<String, T>
     */
    public static <T> Map<String, T> toMap(String jsonString) {
        return toBean(jsonString, new TypeReference<Map<String, T>>() {
        });
    }

    /**
     * 任意对象转换为Map<String,T>类型， String类型另外处理
     *
     * @param obj 对象数据
     * @param <T> 任意类型
     * @return Map<String, T>
     */
    public static <T> Map<String, T> toMap(T obj) {
        return Objects.nonNull(obj) ? toMap(toString(obj)) : new HashMap<>();
    }

    /**
     * 将Json字符串转换为List<Map<String, T>>类型
     *
     * @param jsonString json格式串
     * @param <T>        任意类型
     * @return List<Map < String, T>>
     */
    public static <T> List<Map<String, T>> toListMap(String jsonString) {
        return toBean(formatJson(jsonString, true), new TypeReference<List<Map<String, T>>>() {
        });
    }

    /**
     * 任意对象转List<Map<String, T>>类型，String类型另外处理
     *
     * @param obj 对象数据
     * @param <T> 任意类型
     * @return List<Map < String, T>>
     */
    public static <T> List<Map<String, T>> toListMap(T obj) {
        return Objects.nonNull(obj) ? toListMap(toString(obj)) : new ArrayList<>();
    }

    /**
     * 将json字符串转为指定类型的对象
     *
     * @param jsonString json格式串
     * @param clazz      指定转换的对象类型
     * @param <T>        任意类型
     * @return 返回转换后的 T 类型对象
     */
    private static <T> T toBean(String jsonString, Class<T> clazz) {
        try {
            // 单个对象转JSONArray的情况
            return JSON.parseObject(clazz.getTypeName().endsWith("JSONArray")
                    ? formatJson(jsonString, true)
                    : jsonString, clazz);
        } catch (Exception e) {
            LOGGER.error("json格式串转换类型异常, typeName:{}", clazz.getTypeName(), e);
        }
        return null;
    }

    /**
     * 将json字符串转为指定类型的对象
     *
     * @param jsonString    json格式串
     * @param typeReference 指定转换的类型引用
     * @param <T>           任意类型
     * @return 返回转换后的 T 类型对象
     */
    private static <T> T toBean(String jsonString, TypeReference<T> typeReference) {
        try {
            return JSON.parseObject(jsonString, typeReference);
        } catch (Exception e) {
            LOGGER.error("json格式串转换类型失败: type:{}, jsonString:{}", typeReference.getType().getTypeName(), jsonString, e);
        }
        return null;
    }

    /**
     * 格式化JSON串
     * <p>
     * 单个对象想转换成JSONArray对象，json格式是没有前缀"["和后缀"]"的，直接转换成JSONArray会报JSONException异常
     * </p>
     *
     * @param jsonString json串
     * @param isAdd      true添加前、后缀，false删除前、后缀
     * @return 格式化后的json串
     */
    public static String formatJson(String jsonString, boolean isAdd) {
        if (jsonString == null || jsonString.isEmpty()) {
            return jsonString;
        }
        StringBuilder resultString = new StringBuilder(jsonString);
        boolean isArrayPrefix = jsonString.charAt(0) == ARRAY_PREFIX;
        boolean isArraySuffix = jsonString.charAt(jsonString.length() - 1) == ARRAY_SUFFIX;
        if (isAdd) {
            if (!isArrayPrefix) {
                resultString.insert(0, ARRAY_PREFIX);
            }
            if (!isArraySuffix) {
                resultString.append(ARRAY_SUFFIX);
            }
        } else {
            if (isArrayPrefix) {
                resultString.deleteCharAt(0);
            }
            if (isArraySuffix) {
                resultString.deleteCharAt(resultString.length() - 1);
            }
        }
        return resultString.toString();
    }

    /**
     * 验证一个字符串是否是合法的JSON串
     *
     * @param str 字符串
     * @return true-合法 ，false-非法
     */
    public static boolean isValid(String str) {
        return Objects.nonNull(str) && valid(str.trim());
    }

    public static boolean isValid(byte[] bytes) {
        return isValid(new String(bytes));
    }

    public static boolean isValid(char[] chars) {
        return isValid(new String(chars));
    }

    /**
     * JSON串校验具体实现
     *
     * @param str 校验内容
     * @return true or false
     */
    private static boolean valid(String str) {
        if (str.isEmpty()) {
            return true;
        }
        //初始化
        init(str);
        //校验值
        if (!checkValue()) {
            return error("value", 1);
        } else {
            skipWhiteSpace();
            return currentChar == CharacterIterator.DONE || error("end", currentCol);
        }
    }

    /**
     * 初始化数据
     */
    private static void init(String str) {
        //初始化字符串迭代器
        iterator = new StringCharacterIterator(str);
        //当前迭代字符
        currentChar = iterator.first();
        //当前迭代索引列
        currentCol = 1;
    }

    /**
     * 检测值的合法性
     *
     * @return true or false
     */
    private static boolean checkValue() {
        return checkLiteral("true") //字面值 true
                || checkLiteral("false") //字面值 false
                || checkLiteral("null") //字面值 null
                || checkString() //字符串值
                || checkNumber() //数值
                || checkObject() //对象值
                || checkArray(); //数组值
    }

    /**
     * 校验字面量合法性
     *
     * @param value 值
     * @return true or false
     */
    private static boolean checkLiteral(String value) {
        CharacterIterator ci = new StringCharacterIterator(value);
        char firstChar = ci.first();
        if (currentChar != firstChar) {
            return false;
        }
        int startIndex = currentCol;
        boolean isSuccess = true;
        for (firstChar = ci.next(); firstChar != CharacterIterator.DONE; ci.next()) {
            if (firstChar != next()) {
                isSuccess = false;
                break;
            }
        }
        next(); //移动到结束字符位置
        return isSuccess || error("checkLiteral " + value, startIndex);
    }

    /**
     * 校验数组[]类型合法性
     *
     * @return true or false
     */
    private static boolean checkArray() {
        return scanner0(ARRAY_PREFIX, ARRAY_SUFFIX, false);
    }

    /**
     * 校验对象{}类型合法性
     *
     * @return true or false
     */
    private static boolean checkObject() {
        return scanner0(OBJECT_PREFIX, OBJECT_SUFFIX, true);
    }

    /**
     * 扫描 {} []
     *
     * @param startChar    起始字符
     * @param endChar      结束字符
     * @param isScanPrefix 是否做为前缀扫描
     * @return true or false
     */
    private static boolean scanner0(char startChar, char endChar, boolean isScanPrefix) {
        if (currentChar != startChar) {
            return false;
        }
        next();
        skipWhiteSpace();
        if (currentChar == endChar) {
            next();
            return true;
        }
        while (true) {
            if (isScanPrefix) {
                int startIndex = currentCol;
                // {\"age\": 011111}
                if (!checkString()) {
                    return error("string", startIndex);
                }
                skipWhiteSpace();
                if (currentChar != ':') {
                    return error("colon", currentCol);
                }
                next();
                skipWhiteSpace();
            }
            if (checkValue()) {
                skipDigit();//处理 01111 这种情况
                skipWhiteSpace();
                if (currentChar == ',') {
                    next();
                } else if (currentChar == endChar) {
                    break;
                } else {
                    return error("comma or " + endChar, currentCol);
                }
            } else {
                return error("value", currentCol);
            }
            skipWhiteSpace();
        }
        next();
        return true;
    }

    /**
     * 检测数字合法性
     * {\"data\": \"2017-04-21\", \"age\": 14}"
     *
     * @return true or false
     */
    private static boolean checkNumber() {
        if (!Character.isDigit(currentChar) && currentChar != NEGTIVE_SIGN && currentChar != POSITIVE_SIGN) {
            return false;
        }
        int startIndex = currentCol;
        if (currentChar == NEGTIVE_SIGN || currentChar == POSITIVE_SIGN) {
            next();
        }
        // 0111
        if (currentChar == '0') {
            next(); //跳过0字符
        } else if (Character.isDigit(currentChar)) {
            skipDigit();
        } else {
            return error("number", startIndex);
        }
        // \"1.00038\"
        if (currentChar == '.') {
            next();//跳过小数点
            if (Character.isDigit(currentChar)) {
                skipDigit();
            } else {
                return error("number", startIndex);
            }
        }
        // \"1.03E+08\"
        if (currentChar == 'e' || currentChar == 'E') {
            next(); //科学计数
            if (currentChar == POSITIVE_SIGN || currentChar == NEGTIVE_SIGN) {
                next();
            }
            if (Character.isDigit(currentChar)) {
                skipDigit();
            } else {
                return error("number", startIndex);
            }
        }
        return true;
    }

    /**
     * 检查字符串值合法性
     * {\"data\": \"2017-04-21\", \"age\": 14}"
     *
     * @return true or false
     */
    private static boolean checkString() {
        //校验json属性时，必须以引号开头
        if (currentChar != QUOTATION_MARKS) {
            return false;
        }
        int startIndex = currentCol;
        //是否为转义字符
        boolean isEscaped = false;
        // \"data\"
        for (next(); currentChar != CharacterIterator.DONE; next()) {
            if (!isEscaped && currentChar == ESCAPE_CHAR_PREFIX) {
                isEscaped = true;
            } else if (isEscaped) {
                if (!checkEscape()) {
                    return false;
                }
                isEscaped = false;
            } else if (currentChar == QUOTATION_MARKS) { //以引号结尾
                next();
                return true;
            }
        }
        return error("quoted string", startIndex);
    }

    /**
     * 检测是否为转义字符
     *
     * @return true or false
     */
    private static boolean checkEscape() {
        int startIndex = currentCol - 1;
        if (ESCAPE_CHARS.indexOf(currentChar) < 0) {
            return error("非法转义字符序列", startIndex);
        }
        // unicode编码，后跟16进制
        if (currentChar == 'u' && (isHex(next()) || isHex(next()) || isHex(next()) || isHex(next()))) {
            return error("Unicode转义字符序列格式为\\uxxxx", startIndex);
        }
        return true;
    }

    /**
     * 检测十六进制字符合法性
     *
     * @param ignored 校验字符，这里校验的是全局变量c
     * @return true or false
     */
    private static boolean isHex(char ignored) {
        return HEX_CHARS.indexOf(currentChar) < 0;
    }

    /**
     * 获取下一个字符
     *
     * @return 返回下一个字符
     */
    private static char next() {
        currentChar = iterator.next();
        ++currentCol;
        return currentChar;
    }

    /**
     * 跳过空白字符
     */
    private static void skipWhiteSpace() {
        while (Character.isWhitespace(currentChar)) {
            next();
        }
    }

    /**
     * 跳过数字字符
     */
    private static void skipDigit() {
        while (Character.isDigit(currentChar)) {
            next();
        }
    }

    /**
     * 错误信息
     *
     * @param type 类型
     * @param col  列
     * @return false非法
     */
    private static boolean error(String type, int col) {
        LOGGER.error("type: {}, col: {}", type, col);
        return false;
    }
}
