package com.github.akor.util;

import com.github.akor.common.Checks;

import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @Author : AiTao
 * @Create : 2021-12-17 1:20
 * @Description :
 */
public class ArrayUtils {
    //临时数组
    private static final byte[] BYTES = {};
    private static final short[] SHORTS = {};
    private static final int[] INTS = {};
    private static final long[] LONGS = {};
    private static final float[] FLOATS = {};
    private static final double[] DOUBLES = {};
    private static final char[] CHARS = {};
    private static final boolean[] BOOLEANS = {};
    private static final String[] STRINGS = {};
    private static final Object[] OBJECTS = {};
    private static final String NULL = "null";

    private ArrayUtils() {
        throw new RuntimeException("实例化对象异常");
    }

    /**
     * 判空
     *
     * @param arr 剩余参数
     * @return 是否为空
     */
    @SafeVarargs
    public static <T> boolean isEmpty(T... arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isEmpty(Object arr) {
        if (arr == null) {
            return true;
        } else if (isArray(arr)) {
            return Array.getLength(arr) == 0;
        }
        throw new RuntimeException("Object to provide is not a Array!");
    }

    @SafeVarargs
    public static <T> boolean isNotEmpty(T... arr) {
        return !isEmpty(arr);
    }

    /**
     * 对象是否为数组对象
     *
     * @param obj 对象
     * @return 是否为数组对象，如果为{@code null} 返回false
     */
    public static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    /**
     * 实例化指定大小数组
     *
     * @param clazz 数组类型，只接收对象类型
     * @param size  数组大小
     * @param <T>   任意类型
     * @return 实例化数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Class<?> clazz, int size) {
        return (clazz == Object.class) ? (T[]) new Object[size] : (T[]) Array.newInstance(clazz, size);
    }

    @SafeVarargs
    public static <T> T[] newArray(T... array) {
        return newArray(array, array.length);
    }

    public static <T> T[] newArray(T[] array, int size) {
        return newArray(array, size, array.getClass().getComponentType());
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(T[] array, int size, Class<?> clazz) {
        return (T[]) newArray((Object) array, size, clazz);
    }

    public static Object newArray(Object array, int size, Class<?> clazz) {
        if (clazz.isPrimitive()) {
            final String name = clazz.getName();
            switch (name) {
                case "byte":
                    byte[] bytes = new byte[size];
                    System.arraycopy(array, 0, bytes, 0, size);
                    return bytes;
                case "short":
                    short[] shorts = new short[size];
                    System.arraycopy(array, 0, shorts, 0, size);
                    return shorts;
                case "int":
                    int[] ints = new int[size];
                    System.arraycopy(array, 0, ints, 0, size);
                    return ints;
                case "long":
                    long[] longs = new long[size];
                    System.arraycopy(array, 0, longs, 0, size);
                    return longs;
                case "float":
                    float[] floats = new float[size];
                    System.arraycopy(array, 0, floats, 0, size);
                    return floats;
                case "double":
                    double[] doubles = new double[size];
                    System.arraycopy(array, 0, doubles, 0, size);
                    return doubles;
                case "boolean":
                    boolean[] bools = new boolean[size];
                    System.arraycopy(array, 0, bools, 0, size);
                    return bools;
                default:
                    throw new RuntimeException("不支持的数据类型");
            }
        }
        Object[] newArr = newArray(clazz, size);
        if (isNotEmpty(array) && isArray(array)) {
            Object[] arr = (Object[]) array;
            System.arraycopy(arr, 0, newArr, 0, Math.min(arr.length, size));
        }
        return newArr;
    }

    /**
     * 添加一个或多个元素
     *
     * @param elements 元素
     * @param <T>      值类型
     * @return {@link T[]}
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] addAll(T... elements) {
        Collection<T> c = addAll(new LinkedList<>(), elements);
        return Checks.isNotEmpty(c) ? c.toArray(newArray(elements)) : (T[]) OBJECTS;
    }

    @SafeVarargs
    public static <T> List<T> asList(T... obj) {
        return (List<T>) addAll(new ArrayList<>(), obj);
    }

    @SafeVarargs
    private static <T> Collection<T> addAll(Collection<T> c, T... elements) {
        foreach(elements, c::add);
        return c;
    }

    /**
     * 构造一个ArrayList
     *
     * @param iter {@link Iterator}
     * @param <T>  集合元素类型
     * @return {@link List}
     */
    public static <T> List<T> newList(Iterator<T> iter) {
        List<T> list = new ArrayList<>();
        if (iter == null) {
            return list;
        }
        while (iter.hasNext()) {
            list.add(iter.next());
        }
        return list;
    }

    public static <T> List<T> newList(int size) {
        return new ArrayList<>(size);
    }

    public static <T> List<T> newList(Collection<T> c) {
        return new ArrayList<>(c);
    }

    /**
     * 将集合转为数组
     *
     * @param iter  {@link Iterator}
     * @param clazz 集合元素类型
     * @param <T>   数组元素类型
     * @return {@link T[]}
     */
    public static <T> T[] toArray(Iterator<T> iter, Class<T> clazz) {
        return toArray(newList(iter), clazz);
    }

    public static <T> T[] toArray(Iterable<T> iter, Class<T> clazz) {
        return toArray(toCollection(iter), clazz);
    }

    public static <T> T[] toArray(Collection<T> c, Class<T> clazz) {
        return c.toArray(newArray(clazz, c.size()));
    }

    /**
     * 转换集合
     *
     * @param iter {@link Iterable}
     * @param <E>  集合元素类型
     * @return {@link Collection}
     */
    public static <E> Collection<E> toCollection(Iterable<E> iter) {
        return (iter instanceof Collection) ? (Collection<E>) iter : newList(iter.iterator());
    }

    public static <T> List<T> toList(T[] array) {
        return toList(array, new ArrayList<>());
    }

    public static <T> List<T> toList(T[] array, Collection<T> c) {
        return (List<T>) addAll(c, array);
    }

    public static <V> Map<Integer, V> toMap(V[] array) {
        Map<Integer, V> map = new HashMap<>();
        for (int i = 0; i < array.length; i++) {
            map.put(i, array[i]);
        }
        return map;
    }

    /**
     * 转换字符串
     *
     * @param obj 对象
     * @return 字符串
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return NULL;
        }
        if (isArray(obj)) {
            try {
                //处理对象数组
                return Arrays.deepToString((Object[]) obj);
            } catch (Exception e) {
                //obj.getClass().getComponentType():得到数组类型中元素的Class
                //处理基本类型数组
                switch (obj.getClass().getComponentType().getName()) {
                    case "long":
                        return Arrays.toString((long[]) obj);
                    case "int":
                        return Arrays.toString((int[]) obj);
                    case "short":
                        return Arrays.toString((short[]) obj);
                    case "char":
                        return Arrays.toString((char[]) obj);
                    case "byte":
                        return Arrays.toString((byte[]) obj);
                    case "boolean":
                        return Arrays.toString((boolean[]) obj);
                    case "float":
                        return Arrays.toString((float[]) obj);
                    case "double":
                        return Arrays.toString((double[]) obj);
                    default:
                        throw new RuntimeException(e);
                }
            }
        }
        return obj.toString();
    }

    /**
     * 基本数据类型数据转换成字符串数组
     *
     * @param array 数组
     * @return {@link String[]}
     */
    public static String[] toStringArray(int[] array) {
        if (isEmpty(array)) {
            return STRINGS;
        }
        String[] newArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = String.valueOf(array[i]);
        }
        return newArray;
    }

    public static String[] toStringArray(double[] array) {
        if (isEmpty(array)) {
            return STRINGS;
        }
        String[] newArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = String.valueOf(array[i]);
        }
        return newArray;
    }

    public static String[] toStringArray(float[] array) {
        if (isEmpty(array)) {
            return STRINGS;
        }
        String[] newArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = String.valueOf(array[i]);
        }
        return newArray;
    }

    public static String[] toStringArray(short[] array) {
        if (isEmpty(array)) {
            return STRINGS;
        }
        String[] newArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = String.valueOf(array[i]);
        }
        return newArray;
    }

    public static String[] toStringArray(byte[] array) {
        if (isEmpty(array)) {
            return STRINGS;
        }
        String[] newArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = String.valueOf(array[i]);
        }
        return newArray;
    }

    public static String[] toStringArray(long[] array) {
        if (isEmpty(array)) {
            return STRINGS;
        }
        String[] newArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = String.valueOf(array[i]);
        }
        return newArray;
    }

    public static String[] toStringArray(boolean[] array) {
        if (isEmpty(array)) {
            return STRINGS;
        }
        String[] newArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = String.valueOf(array[i]);
        }
        return newArray;
    }

    public static String[] toStringArray(char[] array) {
        if (isEmpty(array)) {
            return STRINGS;
        }
        String[] newArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = String.valueOf(array[i]);
        }
        return newArray;
    }

    public static String[] toStringArray(Object[] array) {
        if (isEmpty(array)) {
            return STRINGS;
        }
        String[] newArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = String.valueOf(array[i]);
        }
        return newArray;
    }

    public static String[] toStringArray(Collection<String> c) {
        return c != null ? c.toArray(new String[c.size()]) : STRINGS;
    }

    public static int[] toIntArray(String[] array) {
        if (isEmpty(array)) {
            return INTS;
        }
        int[] newArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = Integer.parseInt(array[i]);
        }
        return newArray;
    }

    public static int[] toIntArray(double[] array) {
        if (isEmpty(array)) {
            return INTS;
        }
        int[] newArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (int) array[i];
        }
        return newArray;
    }

    public static int[] toIntArray(float[] array) {
        if (isEmpty(array)) {
            return INTS;
        }
        int[] newArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (int) array[i];
        }
        return newArray;
    }

    public static int[] toIntArray(short[] array) {
        if (isEmpty(array)) {
            return INTS;
        }
        int[] newArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static int[] toIntArray(byte[] array) {
        if (isEmpty(array)) {
            return INTS;
        }
        int[] newArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static int[] toIntArray(long[] array) {
        if (isEmpty(array)) {
            return INTS;
        }
        int[] newArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (int) array[i];
        }
        return newArray;
    }

    public static int[] toIntArray(char[] array) {
        if (isEmpty(array)) {
            return INTS;
        }
        int[] newArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (int) array[i];
        }
        return newArray;
    }

    public static int[] toIntArray(boolean[] array) {
        if (isEmpty(array)) {
            return INTS;
        }
        int[] newArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i] ? 1 : 0;
        }
        return newArray;
    }

    public static int[] toIntArray(Object[] array) {
        if (isEmpty(array)) {
            return INTS;
        }
        int[] newArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                newArray[i] = (int) array[i];
            }
        }
        return newArray;
    }

    public static double[] toDoubleArray(String[] array) {
        if (isEmpty(array)) {
            return DOUBLES;
        }
        double[] newArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = Double.parseDouble(array[i]);
        }
        return newArray;
    }

    public static double[] toDoubleArray(int[] array) {
        if (isEmpty(array)) {
            return DOUBLES;
        }
        double[] newArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static double[] toDoubleArray(float[] array) {
        if (isEmpty(array)) {
            return DOUBLES;
        }
        double[] newArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static double[] toDoubleArray(long[] array) {
        if (isEmpty(array)) {
            return DOUBLES;
        }
        double[] newArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static double[] toDoubleArray(short[] array) {
        if (isEmpty(array)) {
            return DOUBLES;
        }
        double[] newArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static double[] toDoubleArray(byte[] array) {
        if (isEmpty(array)) {
            return DOUBLES;
        }
        double[] newArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static double[] toDoubleArray(char[] array) {
        if (isEmpty(array)) {
            return DOUBLES;
        }
        double[] newArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static double[] toDoubleArray(Object[] array) {
        if (isEmpty(array)) {
            return DOUBLES;
        }
        double[] newArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                newArray[i] = (double) array[i];
            }
        }
        return newArray;
    }

    public static float[] toFloatArray(String[] array) {
        if (isEmpty(array)) {
            return FLOATS;
        }
        float[] newArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = Float.parseFloat(array[i]);
        }
        return newArray;
    }

    public static float[] toFloatArray(int[] array) {
        if (isEmpty(array)) {
            return FLOATS;
        }
        float[] newArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static float[] toFloatArray(double[] array) {
        if (isEmpty(array)) {
            return FLOATS;
        }
        float[] newArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (float) array[i];
        }
        return newArray;
    }

    public static float[] toFloatArray(short[] array) {
        if (isEmpty(array)) {
            return FLOATS;
        }
        float[] newArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static float[] toFloatArray(byte[] array) {
        if (isEmpty(array)) {
            return FLOATS;
        }
        float[] newArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static float[] toFloatArray(long[] array) {
        if (isEmpty(array)) {
            return FLOATS;
        }
        float[] newArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static float[] toFloatArray(char[] array) {
        if (isEmpty(array)) {
            return FLOATS;
        }
        float[] newArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static float[] toFloatArray(Object[] array) {
        if (isEmpty(array)) {
            return FLOATS;
        }
        float[] newArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                newArray[i] = (float) array[i];
            }
        }
        return newArray;
    }

    public static long[] toLongArray(String[] array) {
        if (isEmpty(array)) {
            return LONGS;
        }
        long[] newArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = Long.parseLong(array[i]);
        }
        return newArray;
    }

    public static long[] toLongArray(int[] array) {
        if (isEmpty(array)) {
            return LONGS;
        }
        long[] newArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static long[] toLongArray(double[] array) {
        if (isEmpty(array)) {
            return LONGS;
        }
        long[] newArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (long) array[i];
        }
        return newArray;
    }

    public static long[] toLongArray(float[] array) {
        if (isEmpty(array)) {
            return LONGS;
        }
        long[] newArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (long) array[i];
        }
        return newArray;
    }

    public static long[] toLongArray(short[] array) {
        if (isEmpty(array)) {
            return LONGS;
        }
        long[] newArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static long[] toLongArray(byte[] array) {
        if (isEmpty(array)) {
            return LONGS;
        }
        long[] newArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static long[] toLongArray(char[] array) {
        if (isEmpty(array)) {
            return LONGS;
        }
        long[] newArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static long[] toLongArray(Object[] array) {
        if (isEmpty(array)) {
            return LONGS;
        }
        long[] newArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                newArray[i] = (long) array[i];
            }
        }
        return newArray;
    }

    public static short[] toShortArray(String[] array) {
        if (isEmpty(array)) {
            return SHORTS;
        }
        short[] newArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = Short.parseShort(array[i]);
        }
        return newArray;
    }

    public static short[] toShortArray(int[] array) {
        if (isEmpty(array)) {
            return SHORTS;
        }
        short[] newArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (short) array[i];
        }
        return newArray;
    }

    public static short[] toShortArray(double[] array) {
        if (isEmpty(array)) {
            return SHORTS;
        }
        short[] newArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (short) array[i];
        }
        return newArray;
    }

    public static short[] toShortArray(float[] array) {
        if (isEmpty(array)) {
            return SHORTS;
        }
        short[] newArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (short) array[i];
        }
        return newArray;
    }

    public static short[] toShortArray(long[] array) {
        if (isEmpty(array)) {
            return SHORTS;
        }
        short[] newArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (short) array[i];
        }
        return newArray;
    }

    public static short[] toShortArray(byte[] array) {
        if (isEmpty(array)) {
            return SHORTS;
        }
        short[] newArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static short[] toShortArray(char[] array) {
        if (isEmpty(array)) {
            return SHORTS;
        }
        short[] newArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (short) array[i];
        }
        return newArray;
    }

    public static short[] toShortArray(Object[] array) {
        if (isEmpty(array)) {
            return SHORTS;
        }
        short[] newArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                newArray[i] = (short) array[i];
            }
        }
        return newArray;
    }

    public static byte[] toByteArray(String[] array) {
        if (isEmpty(array)) {
            return BYTES;
        }
        byte[] newArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = Byte.parseByte(array[i]);
        }
        return newArray;
    }

    public static byte[] toByteArray(int[] array) {
        if (isEmpty(array)) {
            return BYTES;
        }
        byte[] newArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (byte) array[i];
        }
        return newArray;
    }

    public static byte[] toByteArray(double[] array) {
        if (isEmpty(array)) {
            return BYTES;
        }
        byte[] newArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (byte) array[i];
        }
        return newArray;
    }

    public static byte[] toByteArray(float[] array) {
        if (isEmpty(array)) {
            return BYTES;
        }
        byte[] newArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (byte) array[i];
        }
        return newArray;
    }

    public static byte[] toByteArray(long[] array) {
        if (isEmpty(array)) {
            return BYTES;
        }
        byte[] newArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (byte) array[i];
        }
        return newArray;
    }

    public static byte[] toByteArray(short[] array) {
        if (isEmpty(array)) {
            return BYTES;
        }
        byte[] newArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (byte) array[i];
        }
        return newArray;
    }

    public static byte[] toByteArray(char[] array) {
        if (isEmpty(array)) {
            return BYTES;
        }
        byte[] newArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (byte) array[i];
        }
        return newArray;
    }

    public static byte[] toByteArray(Object[] array) {
        if (isEmpty(array)) {
            return BYTES;
        }
        byte[] newArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                newArray[i] = (byte) array[i];
            }
        }
        return newArray;
    }

    public static Byte[] toByteArray(byte[] array) {
        if (isEmpty(array)) {
            return new Byte[0];
        }
        Byte[] bytes = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bytes[i] = array[i];
        }
        return bytes;
    }

    /**
     * 装箱操作
     *
     * @param array 数组
     * @return {@link Integer}
     */
    public static Boolean[] toBooleanArray(boolean[] array) {
        if (isEmpty(array)) {
            return new Boolean[0];
        }
        Boolean[] bools = new Boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            bools[i] = array[i];
        }
        return bools;
    }

    public static Character[] toCharacterArray(char[] array) {
        if (isEmpty(array)) {
            return new Character[0];
        }
        Character[] chars = new Character[array.length];
        for (int i = 0; i < array.length; i++) {
            chars[i] = array[i];
        }
        return chars;
    }

    public static Short[] toShortArray(short[] array) {
        if (isEmpty(array)) {
            return new Short[0];
        }
        Short[] shorts = new Short[array.length];
        for (int i = 0; i < array.length; i++) {
            shorts[i] = array[i];
        }
        return shorts;
    }

    public static Integer[] toIntegerArray(int[] array) {
        if (isEmpty(array)) {
            return new Integer[0];
        }
        Integer[] ints = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            ints[i] = array[i];
        }
        return ints;
    }

    public static Long[] toLongArray(long[] array) {
        if (isEmpty(array)) {
            return new Long[0];
        }
        Long[] longs = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            longs[i] = array[i];
        }
        return longs;
    }

    public static Float[] toFloatArray(float[] array) {
        if (isEmpty(array)) {
            return new Float[0];
        }
        Float[] floats = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            floats[i] = array[i];
        }
        return floats;
    }

    public static Double[] toDoubleArray(double[] array) {
        if (isEmpty(array)) {
            return new Double[0];
        }
        Double[] doubles = new Double[array.length];
        foreach(doubles, e -> e = array[0]);
        for (int i = 0; i < array.length; i++) {
            doubles[i] = array[i];
        }
        return doubles;
    }

    /**
     * 元素去重
     *
     * @param array 数组
     * @param <T>   任意对象类型
     * @return 去重后的数组
     */
    public static <T> T[] distinct(T[] array) {
        Arrays.sort(array);
        int idx = 1;
        for (int i = 1; i < array.length; i++) {
            if (array[i] != array[i - 1]) {
                array[idx++] = array[i];
            }
        }
        return newArray(array, idx);
    }

    /**
     * 去除空元素，包含null,空串
     *
     * @param array 数组
     * @param <T>   任意对象类型
     * @return 返回不含非空元素的数组
     */
    public static <T extends CharSequence> T[] removeByEmpty(T[] array) {
        List<T> list = newList(array.length);
        foreach(array, ele -> {
            if (Checks.isNotBlank(ele)) {
                list.add(ele);
            }
        });
        return list.toArray(newArray(array, list.size()));
    }

    /**
     * foreach循环封装
     *
     * @param array  遍历数组
     * @param action 消费型接口
     * @param <T>    任意对象类型
     */
    public static <T> void foreach(T[] array, Consumer<T> action) {
        Objects.requireNonNull(action);
        for (T t : array) {
            action.accept(t);
        }
    }

    public static void foreach(byte[] array, Consumer<Byte> action) {
        Objects.requireNonNull(action);
        for (byte t : array) {
            action.accept(t);
        }
    }

    public static void foreach(short[] array, Consumer<Short> action) {
        Objects.requireNonNull(action);
        for (short t : array) {
            action.accept(t);
        }
    }

    public static void foreach(int[] array, Consumer<Integer> action) {
        Objects.requireNonNull(action);
        for (int t : array) {
            action.accept(t);
        }
    }

    public static void foreach(long[] array, Consumer<Long> action) {
        Objects.requireNonNull(action);
        for (long t : array) {
            action.accept(t);
        }
    }

    public static void foreach(float[] array, Consumer<Float> action) {
        Objects.requireNonNull(action);
        for (float t : array) {
            action.accept(t);
        }
    }

    public static void foreach(double[] array, Consumer<Double> action) {
        Objects.requireNonNull(action);
        for (double t : array) {
            action.accept(t);
        }
    }

    /**
     * 实现js中数组内置方法map
     *
     * @param array  数组
     * @param action 回调方法
     * @param <T>    数组元素类型
     * @param <R>    返回值元素类型
     * @return {@link R[]}
     */
    @SuppressWarnings("unchecked")
    public static <T, R> R[] map(T[] array, Function<T, R> action) {
        List<R> list = new ArrayList<>();
        foreach(array, e -> list.add(action.apply(e)));
        return (R[]) list.toArray();
    }

    /**
     * 实现js中数组内置方法filter
     *
     * @param array  数组
     * @param action 过滤条件
     * @param <T>    元素返回类型
     * @return {@link T[]}
     */
    public static <T> T[] filter(T[] array, Predicate<T> action) {
        List<T> list = new ArrayList<>();
        foreach(array, t -> {
            if (action.test(t)) {
                list.add(t);
            }
        });
        return list.toArray(newArray(array));
    }

    /**
     * 截取子数组
     *
     * @param array 数组集合
     * @return 子数组
     */
    public static byte[] subarray(byte[] array, int startIndex) {
        return subarray(array, startIndex, array.length);
    }

    public static byte[] subarray(byte[] array, int startIndex, int endIndex) {
        if (isEmpty(array)) {
            return BYTES;
        }
        int subLen = checkArrayIndexOutOfBoundsException(startIndex, endIndex, array.length);
        byte[] newArr = new byte[subLen];
        System.arraycopy(array, startIndex, newArr, 0, newArr.length);
        return newArr;
    }

    public static short[] subarray(short[] array, int startIndex) {
        return subarray(array, startIndex, array.length);
    }

    public static short[] subarray(short[] array, int startIndex, int endIndex) {
        if (isEmpty(array)) {
            return SHORTS;
        }
        int subLen = checkArrayIndexOutOfBoundsException(startIndex, endIndex, array.length);
        short[] newArr = new short[subLen];
        System.arraycopy(array, startIndex, newArr, 0, newArr.length);
        return newArr;
    }

    public static int[] subarray(int[] array, int startIndex) {
        return subarray(array, startIndex, array.length);
    }

    public static int[] subarray(int[] array, int startIndex, int endIndex) {
        if (isEmpty(array)) {
            return INTS;
        }
        int subLen = checkArrayIndexOutOfBoundsException(startIndex, endIndex, array.length);
        int[] newArr = new int[subLen];
        System.arraycopy(array, startIndex, newArr, 0, newArr.length);
        return newArr;
    }

    public static long[] subarray(long[] array, int startIndex) {
        return subarray(array, startIndex, array.length);
    }

    public static long[] subarray(long[] array, int startIndex, int endIndex) {
        if (isEmpty(array)) {
            return LONGS;
        }
        int subLen = checkArrayIndexOutOfBoundsException(startIndex, endIndex, array.length);
        long[] newArr = new long[subLen];
        System.arraycopy(array, startIndex, newArr, 0, newArr.length);
        return newArr;
    }

    public static float[] subarray(float[] array, int startIndex) {
        return subarray(array, startIndex, array.length);
    }

    public static float[] subarray(float[] array, int startIndex, int endIndex) {
        if (isEmpty(array)) {
            return FLOATS;
        }
        int subLen = checkArrayIndexOutOfBoundsException(startIndex, endIndex, array.length);
        float[] newArr = new float[subLen];
        System.arraycopy(array, startIndex, newArr, 0, newArr.length);
        return newArr;
    }

    public static double[] subarray(double[] array, int startIndex) {
        return subarray(array, startIndex, array.length);
    }

    public static double[] subarray(double[] array, int startIndex, int endIndex) {
        if (isEmpty(array)) {
            return DOUBLES;
        }
        int subLen = checkArrayIndexOutOfBoundsException(startIndex, endIndex, array.length);
        double[] newArr = new double[subLen];
        System.arraycopy(array, startIndex, newArr, 0, newArr.length);
        return newArr;
    }

    public static char[] subarray(char[] array, int startIndex) {
        return subarray(array, startIndex, array.length);
    }

    public static char[] subarray(char[] array, int startIndex, int endIndex) {
        if (isEmpty(array)) {
            return CHARS;
        }
        int subLen = checkArrayIndexOutOfBoundsException(startIndex, endIndex, array.length);
        char[] newArr = new char[subLen];
        System.arraycopy(array, startIndex, newArr, 0, newArr.length);
        return newArr;
    }

    public static boolean[] subarray(boolean[] array, int startIndex) {
        return subarray(array, startIndex, array.length);
    }

    public static boolean[] subarray(boolean[] array, int startIndex, int endIndex) {
        if (isEmpty(array)) {
            return BOOLEANS;
        }
        int subLen = checkArrayIndexOutOfBoundsException(startIndex, endIndex, array.length);
        boolean[] newArr = new boolean[subLen];
        System.arraycopy(array, startIndex, newArr, 0, newArr.length);
        return newArr;
    }

    public static <T> T[] subarray(T[] array, int startIndex) {
        return subarray(array, startIndex, array.length);
    }

    public static <T> T[] subarray(T[] array, int startIndex, int endIndex) {
        if (isEmpty(array)) {
            return null;
        }
        int subLen = checkArrayIndexOutOfBoundsException(startIndex, endIndex, array.length);
        T[] newArr = newArray(array, subLen);
        System.arraycopy(array, startIndex, newArr, 0, newArr.length);
        return newArr;
    }

    /**
     * 反转数组元素
     *
     * @param array      数组
     * @param startIndex 开始索引
     * @param endIndex   结束索引
     * @return true成功, false失败
     */
    public static <T> void reverse(T[] array, int startIndex, int endIndex) {
        if (isNotEmpty(array)) {
            checkArrayIndexOutOfBoundsException(startIndex);
            checkArrayIndexOutOfBoundsException(endIndex, array.length);
            int[] idx = {startIndex, endIndex};
            if (startIndex > endIndex) {
                swap(idx, 0, 1);
            }
            for (int i = idx[0], j = idx[1] - 1; i < j; i++, j--) {
                swap(array, i, j);
            }
        }
    }

    public static <T> void reverse(T[] array, int startIndex) {
        reverse(array, startIndex, array.length);
    }

    public static <T> void reverse(T[] array) {
        reverse(array, 0);
    }

    public static void reverse(byte[] array, int startIndex, int endIndex) {
        if (isNotEmpty(array)) {
            checkArrayIndexOutOfBoundsException(startIndex);
            checkArrayIndexOutOfBoundsException(endIndex, array.length);
            int[] idx = {startIndex, endIndex};
            if (startIndex > endIndex) {
                swap(idx, 0, 1);
            }
            for (int i = idx[0], j = idx[1] - 1; i < j; i++, j--) {
                swap(array, i, j);
            }
        }
    }

    public static void reverse(byte[] array, int startIndex) {
        reverse(array, startIndex, array.length);
    }

    public static void reverse(byte[] array) {
        reverse(array, 0);
    }

    public static void reverse(short[] array, int startIndex, int endIndex) {
        if (isNotEmpty(array)) {
            checkArrayIndexOutOfBoundsException(startIndex);
            checkArrayIndexOutOfBoundsException(endIndex, array.length);
            int[] idx = {startIndex, endIndex};
            if (startIndex > endIndex) {
                swap(idx, 0, 1);
            }
            for (int i = idx[0], j = idx[1] - 1; i < j; i++, j--) {
                swap(array, i, j);
            }
        }
    }

    public static void reverse(short[] array, int startIndex) {
        reverse(array, startIndex, array.length);
    }

    public static void reverse(short[] array) {
        reverse(array, 0);
    }

    public static void reverse(int[] array, int startIndex, int endIndex) {
        if (isNotEmpty(array)) {
            checkArrayIndexOutOfBoundsException(startIndex);
            checkArrayIndexOutOfBoundsException(endIndex, array.length);
            int[] idx = {startIndex, endIndex};
            if (startIndex > endIndex) {
                swap(idx, 0, 1);
            }
            for (int i = idx[0], j = idx[1] - 1; i < j; i++, j--) {
                swap(array, i, j);
            }
        }
    }

    public static void reverse(int[] array, int startIndex) {
        reverse(array, startIndex, array.length);
    }

    public static void reverse(int[] array) {
        reverse(array, 0);
    }

    public static void reverse(long[] array, int startIndex, int endIndex) {
        if (isNotEmpty(array)) {
            checkArrayIndexOutOfBoundsException(startIndex);
            checkArrayIndexOutOfBoundsException(endIndex, array.length);
            int[] idx = {startIndex, endIndex};
            if (startIndex > endIndex) {
                swap(idx, 0, 1);
            }
            for (int i = idx[0], j = idx[1] - 1; i < j; i++, j--) {
                swap(array, i, j);
            }
        }
    }

    public static void reverse(long[] array, int startIndex) {
        reverse(array, startIndex, array.length);
    }

    public static void reverse(long[] array) {
        reverse(array, 0);
    }

    public static void reverse(float[] array, int startIndex, int endIndex) {
        if (isNotEmpty(array)) {
            checkArrayIndexOutOfBoundsException(startIndex);
            checkArrayIndexOutOfBoundsException(endIndex, array.length);
            int[] idx = {startIndex, endIndex};
            if (startIndex > endIndex) {
                swap(idx, 0, 1);
            }
            for (int i = idx[0], j = idx[1] - 1; i < j; i++, j--) {
                swap(array, i, j);
            }
        }
    }

    public static void reverse(float[] array, int startIndex) {
        reverse(array, startIndex, array.length);
    }

    public static void reverse(float[] array) {
        reverse(array, 0);
    }

    public static void reverse(double[] array, int startIndex, int endIndex) {
        if (isNotEmpty(array)) {
            checkArrayIndexOutOfBoundsException(startIndex);
            checkArrayIndexOutOfBoundsException(endIndex, array.length);
            int[] idx = {startIndex, endIndex};
            if (startIndex > endIndex) {
                swap(idx, 0, 1);
            }
            for (int i = idx[0], j = idx[1] - 1; i < j; i++, j--) {
                swap(array, i, j);
            }
        }
    }

    public static void reverse(double[] array, int startIndex) {
        reverse(array, startIndex, array.length);
    }

    public static void reverse(double[] array) {
        reverse(array, 0);
    }

    public static void reverse(char[] array, int startIndex, int endIndex) {
        if (isNotEmpty(array)) {
            checkArrayIndexOutOfBoundsException(startIndex);
            checkArrayIndexOutOfBoundsException(endIndex, array.length);
            int[] idx = {startIndex, endIndex};
            if (startIndex > endIndex) {
                swap(idx, 0, 1);
            }
            for (int i = idx[0], j = idx[1] - 1; i < j; i++, j--) {
                swap(array, i, j);
            }
        }
    }

    public static void reverse(char[] array, int startIndex) {
        reverse(array, startIndex, array.length);
    }

    public static void reverse(char[] array) {
        reverse(array, 0);
    }

    /**
     * 交换数组元素
     *
     * @param array 数组
     * @param i     元素位置
     * @param j     元素位置
     */
    public static void swap(Object[] array, int i, int j) {
        if (isEmpty(array)) {
            return;
        }
        Object tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(byte[] array, int i, int j) {
        if (isEmpty(array)) {
            return;
        }
        byte tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(short[] array, int i, int j) {
        if (isEmpty(array)) {
            return;
        }
        short tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(char[] array, int i, int j) {
        if (isEmpty(array)) {
            return;
        }
        char tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(int[] array, int i, int j) {
        if (isEmpty(array)) {
            return;
        }
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(long[] array, int i, int j) {
        if (isEmpty(array)) {
            return;
        }
        long tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(float[] array, int i, int j) {
        if (isEmpty(array)) {
            return;
        }
        float tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(double[] array, int i, int j) {
        if (isEmpty(array)) {
            return;
        }
        double tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(boolean[] array, int i, int j) {
        if (isEmpty(array)) {
            return;
        }
        boolean tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    /**
     * 二分查找
     *
     * @param array  数组
     * @param target 目标元素
     * @param <T>    元素类型
     * @return 指定元素索引，若目标元素不存在，则返回-1
     */
    @SuppressWarnings("unchecked")
    public static <T> int indexOf(T[] array, T target) {
        if (isEmpty(array)) {
            return -1;
        }
        for (int left = 0, right = array.length - 1, mid; left <= right; ) {
            mid = left + ((right - left) >> 1);
            Comparable<T> cmp = (Comparable<T>) target;
            if (cmp.compareTo(array[mid]) > 0) {
                left = mid + 1;
            } else if (cmp.compareTo(array[mid]) < 0) {
                right = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 判断数组中是否包含指定元素
     *
     * @param array  数组
     * @param target 目标元素
     * @param <T>    元素类型
     * @return true or false
     */
    public static <T> boolean contains(T[] array, T target) {
        return indexOf(array, target) != -1;
    }

    /**
     * 数组错排
     *
     * @param array 数组
     * @param <T>   元素类型
     * @return 打乱后的数组
     */
    public <T> T[] shuffle(T[] array) {
        Random random = new SecureRandom();
        for (int index = array.length - 1; index >= 0; index--) {
            swap(array, random.nextInt(index + 1), index);
        }
        return array;
    }

    /**
     * 获取最小值
     *
     * @param array 数字数组
     * @param <T>   元素类型
     * @return 最小值
     */
    @SuppressWarnings("unchecked")
    public static <T> T min(T... array) {
        final Comparable<T>[] min = new Comparable[]{(Comparable<T>) array[0]};
        foreach(array, e -> min[0] = min[0].compareTo(e) > 0 ? (Comparable<T>) e : min[0]);
        return (T) min[0];
    }

    /**
     * 获取最小值
     *
     * @param array 数字数组
     * @param <T>   元素类型
     * @return 最小值
     */
    @SuppressWarnings("unchecked")
    public static <T> T max(T... array) {
        final Comparable<T>[] max = new Comparable[]{(Comparable<T>) array[0]};
        foreach(array, e -> max[0] = max[0].compareTo(e) < 0 ? (Comparable<T>) e : max[0]);
        return (T) max[0];
    }


    public static <T> T[] merge(T[] a, T[] b, boolean isSorted) {
        newArray(a, a.length);
        newArray(b, b.length);
        System.arraycopy(a, 0, b, 0, b.length);
        return null;
    }

    /**
     * 删除指定位置的元素
     *
     * @param array 数组
     * @param index 待删除元素位置
     * @param <T>   元素类型
     * @return 返回被删除的元素值
     */
    public static <T> T remove(T[] array, int index) {
        int size = array.length;
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        //1 2 3 5 6 6
        T oldVal = array[index];
        //6 - 3 - 1 = 2
        //移动的元素个数
        int moveValueNumber = size - index - 1;
        if (moveValueNumber > 0) {
            System.arraycopy(array, index + 1, array, index, moveValueNumber);
        }
        array[--size] = null;
        return oldVal;
    }

    /**
     * 检查数组索引的合法性
     *
     * @param startIndex 开始索引
     * @param endIndex   结束索引
     * @param length     数组长度
     * @return 元素个数
     */
    private static int checkArrayIndexOutOfBoundsException(int startIndex, int endIndex, int length) {
        checkArrayIndexOutOfBoundsException(startIndex);
        checkArrayIndexOutOfBoundsException(endIndex, length);
        int subLen = endIndex - startIndex;
        checkArrayIndexOutOfBoundsException(subLen);
        return subLen;
    }

    private static void checkArrayIndexOutOfBoundsException(int index) {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
    }

    private static void checkArrayIndexOutOfBoundsException(int index, int length) {
        if (index > length) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        int[] arr1 = {1, 2, 3};
        Object obj = newArray(arr1, 3, arr1.getClass().getComponentType());
        if (isArray(obj)) {
            int[] obj1 = (int[]) obj;
            System.out.println("obj1:" + toString(obj1));
        }
        Object[] array = newArray(Integer.class, 10);
        int[] ints = toIntArray(array);
        System.out.println(toString(array));
        System.out.println(toString(ints));
        int[] obj2 = (int[]) Array.newInstance(int.class, 5);
        System.out.println(Arrays.toString(obj2));
        Integer[] arr = newArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        reverse(arr, arr.length, 0);
        System.out.println(toString(arr));
        System.out.println(toString(subarray(toIntArray(arr), 2, 4)));
        String[] strings = removeByEmpty(new String[]{"aitao", "lml", "", "    ", null, "xiao", null});
        foreach(strings, e -> {
            System.out.print(e + " ");
        });

        Integer[] integers = newArray(3, 2, 2, 1, 1, 1, 4, 4, 4, 2, 3);
        Integer[] distinctArr = distinct(integers);
        System.out.println(toString(distinctArr));

        Object[] arrs = {1, 2};
        swap(arrs, 0, 1);
        System.out.println(arrs[0] + " " + arrs[1]);

        String[] strs = addAll("aaaa", "bbbb", "cccc");
        Integer[] integers1 = addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 22, null);
        System.out.println(toString(strs));
        System.out.println(toString(integers1));

        System.out.println(toString(111111));
        System.out.println(toString(null));

        System.out.println(Arrays.toString(map(integers1, e -> e != null ? e * 10 : 0)));
        System.out.println(Arrays.toString(filter(integers1, e -> e != null && e % 2 != 0)));
        System.out.println(Arrays.binarySearch(newArray(1, 2, 3, 4, 5, 6, 7, 8, 9), 9));
        System.out.println(indexOf(newArray(1, 2, 3, 4, 5, 6, 7, 8, 9), 9));
        System.out.println(min(newArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)));
        System.out.println(max(newArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)));
        Integer[] integers2 = {1, 2, 3, 4, 5, 6};
        Integer remValue = remove(integers2, 4);
        System.out.println(remValue);
        System.out.println(Arrays.toString(integers2));
    }
}
