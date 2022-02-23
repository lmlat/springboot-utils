package com.aitao.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/09/29 12:50
 * @Description : 命令行调用工具类
 */
public class CmdUtils {
    /**
     * 调用脚本命令
     *
     * @param command 指令集
     * @return {@link Process}
     */
    public static Cmd call(List<String> command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String[] commends = new String[command.size()];
        command.toArray(commends);
        processBuilder.command(commends);
        try {
            return new Cmd(processBuilder.start());
        } catch (Exception e) {
            throw new RuntimeException("执行错误", e);
        }
    }

    /**
     * 调用脚本命令
     *
     * @param command 指令集
     * @return {@link Process}
     */
    public static Cmd call(String command) {
        try {
            return new Cmd(Runtime.getRuntime().exec(command));
        } catch (IOException e) {
            throw new RuntimeException("执行错误", e);
        }
    }

    public static class Cmd {
        private final Process process;

        public Cmd(Process process) {
            this.process = process;
        }

        /**
         * 获取返回结果
         *
         * @return 输出信息
         */
        public String get() {
            try {
                if (process != null) {
                    return getResult(process.getInputStream(), false);
                }
                return "";
            } catch (Exception e) {
                throw new RuntimeException("执行错误", e);
            }
        }

        public void console() {
            try {
                getResult(process.getInputStream(), true);
                getResult(process.getErrorStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 获取错误信息
         *
         * @return 错误信息
         */
        public String error() {
            try {
                if (process != null) {
                    return getResult(process.getErrorStream(), false);
                }
            } catch (Exception e) {
                throw new RuntimeException("执行错误", e);
            }
            return null;
        }

        /**
         * 获取结果
         *
         * @param is         进程中的输出信息或错误信息
         * @param isRealTime 是否实时输出
         * @return 结果
         * @throws IOException 处理流时可能会异常
         */
        private String getResult(InputStream is, boolean isRealTime) throws IOException {
            //GBK处理中文乱码问题
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "GBK"));
            StringBuffer buff = new StringBuffer("");
            String temp;
            while ((temp = reader.readLine()) != null) {
                if (isRealTime) {
                    System.out.println(temp);
                } else {
                    buff.append(temp).append("\n");
                }
            }
            close(reader);
            return buff.toString().trim();
        }

        /**
         * 关闭流
         */
        public void close(Reader reader) {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        /**
         * 终止进程
         */
        public void destroy() {
            if (process != null) {
                process.destroyForcibly();
            }
        }
    }

    public static void main(String[] args) {
        List<String> commend = new ArrayList<>();
        commend.add("cmd");
        commend.add("/c");
        //commend.add("git");
        //commend.add("rev-parse");
        //commend.add("--short");
        //commend.add("HEAD");
        //commend.add("ping");
        //commend.add("www.baidu.com");
        commend.add("java");
        commend.add("-version");
        //commend.add("proguard");
        //commend.add("@C:\\Users\\10034\\Desktop\\Learn\\springboot-utils\\src\\main\\resources\\proguard.txt");
        CmdUtils.call(commend).console();
    }
}
