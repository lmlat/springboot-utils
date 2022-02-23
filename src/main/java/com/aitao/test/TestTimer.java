package com.aitao.test;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Company QAX
 * @Author : ai.tao
 * @Create : 2021/12/22 18:23
 * @Description : Timer类的使用
 */
public class TestTimer {
    /**
     * Timer中含有三个类：
     * TimerTask
     * TimerThread
     * TaskQueue
     */
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, 3000);
    }
}
