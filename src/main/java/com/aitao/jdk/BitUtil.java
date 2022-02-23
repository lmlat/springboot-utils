package com.aitao.jdk;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/07/14 13:42
 * @Description : 自定义位运算工具包
 * @Version : 1.0
 * 依赖包：无
 */
@SuppressWarnings("all")
public class BitUtil {
    private static Map<Integer, Integer> BIT_MAP;
    private static final char[] HEX_VALUE = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    static {
        BIT_MAP = new HashMap<>();
        BIT_MAP.put(2, 0b1);
        BIT_MAP.put(8, 0b111);
        BIT_MAP.put(16, 0b1111);
    }

    /**
     * 判空
     *
     * @param obj
     * @return
     */
    private boolean isEmpty(Object obj) {
        return obj == null;
    }

    /**
     * 判断奇偶性
     *
     * @param num
     * @return 偶数返回false，奇数返回true
     */
    public boolean isOdd(Integer num) {
        return (num & 1) == 1;
    }

    /**
     * 判断一个数是否是2的正整数幂
     *
     * @return
     */
    public boolean isPowerOfTwo(Integer num) {
        // 0不是2的正整数次
        return (num != 0) && (num & num - 1) == 0;
    }

    /**
     * 获取某个数的第k个二进制位
     *
     * @param num
     * @param k
     * @return
     */
    public int getBit(Integer num, int k) {
        return (num >>> (k - 1)) & 1;
    }

    /**
     * 计算整数平均数
     *
     * @param x
     * @param y
     * @return
     */
    public int average(int x, int y) {
        return (x & y) + ((x ^ y) >> 1);
    }

    /**
     * 求绝对值
     *
     * @param x
     * @return
     */
    public int abs(int x) {
        int y = x >> 31;//-1
        //(-128^-1)=(10000000 00000000 00000000 00000000 ^ 00000000 00000000 00000000 011111111)=127
        return (x ^ y) - y;
    }

    /**
     * 计算二进制位的个数
     *
     * @param num
     * @return
     */
    public int bitCount(int num) {
        int count = 0;
        while (num != 0) {
            // 取出二进制位最右边的1
            int rightOne = num & (~num + 1);
            // 计数
            count++;
            // 与得到的1值异或，清除掉最右边的1
            num ^= rightOne;
        }
        return count;
    }


    /**
     * byte(位) 	    8
     * short(短整数) 	16
     * int(整数) 	    32
     * long(长整数) 	    64
     * float(单精度) 	32
     * double(双精度) 	64
     * char(字符) 	    16
     * boolean(布尔值) 	8
     * 获取指定类型的所占字节位数
     *
     * @return 字节位数
     */
    public int getByte(Class clazz) {
        int byteSize = 0;
        if (clazz == null) {
            return byteSize;
        }
        String type = clazz.getTypeName();
        if (type.endsWith("Byte")) {
            byteSize = Byte.SIZE;
        } else if (type.endsWith("Short")) {
            byteSize = Short.SIZE;
        } else if (type.endsWith("Integer")) {
            byteSize = Integer.SIZE;
        } else if (type.endsWith("Long")) {
            byteSize = Long.SIZE;
        } else if (type.endsWith("Float")) {
            byteSize = Float.SIZE;
        } else if (type.endsWith("Double")) {
            byteSize = Double.SIZE;
        } else if (type.endsWith("Character")) {
            byteSize = Character.SIZE;
        }
        return byteSize;
    }

    /**
     * 2,8,10,16进制转换
     *
     * @param target
     * @param radix
     * @return 返回一个10进制值
     */
    public String toBOH(Integer target, int radix) {
        // 不支持的进制默认转换10进制
        if (!BIT_MAP.containsKey(radix)) {
            radix = 10;
        }
        // 转换成10进制
        if (radix == 10) {
            return String.valueOf(target);
        }
        // 位数
        int bitNum = log2(radix);//2,8,16进制
        //判断空
        if (target == null || target == 0) {
            return "0";
        }
        StringBuilder result = new StringBuilder();
        while (target != 0) {
            //向前插入
            result.insert(0, HEX_VALUE[target & BIT_MAP.get(radix)]);
            target = (target >>> bitNum);
        }
        return result.toString();
    }

    /**
     * 10进制转2进制
     *
     * @param target
     * @return 返回2进制字符串
     */
    public String toBinaryString(Integer target) {
        if (target == null || target == 0) {
            return "0";
        }
        // 定义栈
        Deque<Integer> resultDeque = new LinkedList<>();
        // 每次都和最低位进行与运算,相同则存入1,不同则存入0
        for (int i = Integer.SIZE; i >= 0; i--) {
            // 和最低位与
            resultDeque.addFirst(target & 1);
            // 右移1位z
            target >>>= 1;
        }
        return resultDeque.toString();
    }

    /**
     * 10进制转16进制
     *
     * @param target
     * @return 返回16进制字符串
     */
    private String toHexString(Integer target, int radix) {
        //判断空
        if (target == null || target == 0) {
            return "0";
        }
        StringBuilder result = new StringBuilder();
        while (target != 0) {
            //向前插入
            result.insert(0, HEX_VALUE[target & 0b1111]);
            target = (target >>> 4);
        }
        return result.toString();
    }

    /**
     * 10进制转8进制
     *
     * @param target
     * @return 返回8进制字符串
     */
    private String toOctalString(Integer target) {
        //判断空
        if (target == null || target == 0) {
            return "0";
        }
        // 定义栈
        StringBuilder result = new StringBuilder();
        while (target != 0) {
            result.insert(0, HEX_VALUE[target & 0b111]);
            target = (target >>> 3);
        }
        return result.toString();
    }

    /**
     * 10进制转16进制
     *
     * @param target
     * @return 返回16进制换算后的10进制值
     */
    public int toHex(int target) {
        if (target == 0) {
            return 0;
        }
        int mod = mod(target, 16);
        int devide = toHex(target >>> 4);
        return devide * 10 + mod;
    }

    /**
     * 求余
     *
     * @param a
     * @param b
     */
    public int mod(int a, int b) {
        return a & (b - 1);
    }

    /**
     * 求一个整数是2的几次幂
     *
     * @param target
     * @return
     */
    public int log2(int target) {
        int count = 0;
        while ((target & 1) == 0) {
            count++;
            target >>= 1;
        }
        return count;
    }

    /**
     * 相加
     *
     * @param a
     * @param b
     */
    public int add(int a, int b) {
        int sum = 0;
        while (b != 0) {
            sum = a ^ b;//不进位
            b = (a & b) << 1;//只考虑进位
            a = sum;
        }
        return sum;
    }

    /**
     * 相减
     *
     * @param a
     * @param b
     */
    public int sub(int a, int b) {
        return add(a, -b);
    }

    /**
     * 任意进制转换
     *
     * @param s     转换的值
     * @param radix 2~36进制
     * @return 返回转换指定进制的值
     */
    public String to(Integer number, int radix) {
        //2~36进制
        if (radix < 2 || radix > 36) {
            radix = 10;
        }
        //转10进制
        if (radix == 10) {
            return toBOH(number, 10);
        }
        char buf[] = new char[33];
        // 正、负标记
        boolean negative = (number < 0);
        int charPos = 32;

        // 处理负数
        if (!negative) {
            number = -number;
        }

        while (number <= -radix) {
            buf[charPos] = HEX_VALUE[-(number % radix)];
            charPos--;
            number /= radix;
        }
        buf[charPos] = HEX_VALUE[-number];

        if (negative) {
            charPos--;
            buf[charPos] = '-';
        }
        return new String(buf, charPos, (33 - charPos));
    }
}
