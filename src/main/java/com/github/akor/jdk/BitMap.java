package com.github.akor.jdk;

import java.util.BitSet;

public class BitMap {

    private int[] bitArray;

    //int 占用4个字节,所以size / 32
    public BitMap(long size) {
        bitArray = new int[(int) (size / 32 + 1)];
    }

    /**
     * 寻址:设置比较位为1z
     * (1)确定数组index：index = 119 / 32 = Math.floor(3.7);
     * (2)确定比特位index: index = 119 % 32 = 23,左移 1 << 23
     * (3)设置比特位为1：将1左移bitIndex位，然后与原来的值进行或运算。
     */
    public void set1(int num) {
        //确定数组 index
        int arrayIndex = num >> 5; // num / 32
        //确定比特位 index
        int bitIndex = num & 31;// num % 32
        //设置比特位为1
        bitArray[arrayIndex] |= 1 << bitIndex;
        System.out.println(get32BitBinString(bitArray[arrayIndex]));
    }

    /**
     * 寻址:设置比较位为0
     * (1)确定数组index：index = 119 / 32 = Math.floor(3.7);
     * (2)确定比特位index: index = 119 % 32 = 23,左移 1 << 23
     * (3)设置比特位为0：将1左移bitIndex位，并进行非运算，然后与原来的值进行与运算。
     */
    public void set0(int num) {
        //确定数组 index
        int arrayIndex = num >> 5;// num / 32
        //确定比特位 index
        int bitIndex = num & 31;// num % 32
        //设置比特位为0
        bitArray[arrayIndex] &= ~(1 << bitIndex);
    }

    /**
     * 寻址:判断指定比特位是否存在元素
     * (1)确定数组index：index = 119 / 32 = Math.floor(3.7);
     * (2)确定比特位index: index = 119 % 32 = 23,左移 1 << 23
     * (3)设置比特位为0：将1左移bitIndex位，然后与原来的值进行与运算。只要与运算结果中有1，即表示元素存在。
     */
    public boolean isExist(int num) {
        //确定数组 index
        int arrayIndex = num >> 5;
        //确定比特位 index
        int bitIndex = num & 31;
        //判断是否存在
        return (bitArray[arrayIndex] & ((1 << bitIndex))) != 0;
    }

    /**
     * 将整型数字转换为二进制字符串，一共32位，不舍弃前面的0
     *
     * @param number 整型数字
     * @return 二进制字符串
     */
    private static String get32BitBinString(int number) {
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            sBuilder.append(number & 1);
            number = number >>> 1;
        }
        return sBuilder.reverse().toString();
    }

    public static void main(String[] args) {
//        BitSet 位图
        int[] arrays = new int[]{1, 2, 35, 22, 56, 334, 245, 2234, 54};
        testBitMap(arrays);
        testBitSet(arrays);
    }

    private static void testBitSet(int[] arrays) {
        BitSet bitSet = new BitSet(16);
        for (int array : arrays) {
            bitSet.set(array);
        }
        System.out.println(bitSet.get(2234));
    }

    private static void testBitMap(int[] arrays) {
        BitMap bitMapTest = new BitMap(2234);
        for (int i : arrays) {
            bitMapTest.set1(i);
        }
        System.out.println(bitMapTest.isExist(245));
    }
}

