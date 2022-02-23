package com.github.akor.files;

/**
 * @Author : AiTao
 * @Create : 2021-12-11 0:44
 * @Description :
 */
public class Test {
    private static int x = 0;

    public static void main(String[] args) {
        boolean flag = A() || B() || C();
    }

    private static boolean C() {
        x = 3;
        System.out.println(x);
        return false;
    }

    private static boolean B() {
        x = 2;
        System.out.println(x);
        return false;
    }

    private static boolean A() {
        x = 1;
        System.out.println(x);
        return true;
    }
}
