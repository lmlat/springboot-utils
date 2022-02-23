package com.github.akor.files;

import com.github.akor.common.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * @Author : AiTao
 * @Create : 2021-12-01 18:46
 * @Description : 日志文件工具类
 */
public class LogUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtils.class);

    // 项目根路径
    private static final String PROJECT_ROOT_PATH = System.getProperty("user.dir");

    // 存放日志文件的文件夹名称
    private static final String LOGS_DIR_NAME = "logs";

    /**
     * 检测文件是否存在
     *
     * @param file 文件对象
     */
    public static boolean checkAndCreateFile(File file) {
        if (file != null) {
            if (file.exists()) {
                LOGGER.debug("文件[{}]已经存在", file.getPath());
            } else {
                LOGGER.debug("文件[{}]不存在，正在创建...", file.getPath());
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    LOGGER.error("文件[{}]创建失败", file.getPath(), e);
                    return false;
                }
                LOGGER.debug("文件[{}]创建成功", file.getPath());
            }
            return true;
        }
        return false;
    }

    /**
     * 检测文件夹是否存在
     *
     * @param file 文件目录
     */
    public static boolean checkAndCreateDir(File file) {
        if (file != null) {
            if (file.exists()) {
                if (file.isDirectory()) {
                    LOGGER.debug("文件夹[{}]已经存在", file.getName());
                } else {
                    LOGGER.debug("文件夹[{}]存在与此文件夹[{}]同名的文件，不能再创建文件夹文件", file.getPath(), file.getName());
                }
            } else {
                LOGGER.debug("文件夹[{}]不存在，正在创建...", file.getPath());
                file.mkdir();
                LOGGER.debug("文件夹[{}]创建成功", file.getName());
            }
            return true;
        }
        return false;
    }

    /**
     * 导出日志文件
     *
     * @param data 文件内容
     */
    public static void write(String data) {
        File file = new File(PROJECT_ROOT_PATH + File.separator + LOGS_DIR_NAME + File.separator + DateUtils.toString(new Date(), "yyyy-MM-dd") + ".log");
        File dirFile = new File(PROJECT_ROOT_PATH + File.separator + LOGS_DIR_NAME);
        // 检测文件夹或文件是否存在, 不存在则创建
        checkAndCreateFile(dirFile);
        checkAndCreateDir(file);
        FileWriter fw = null;
        BufferedWriter writer = null;
        try {
            fw = new FileWriter(file, true);
            writer = new BufferedWriter(fw);
            String content = DateUtils.toString(new Date(), "yyyy/MM/dd E HH:mm:ss:S") + "\t\t" + data;
            writer.write(content);
            writer.newLine();// 换行
            writer.flush();
        } catch (IOException e) {
            LOGGER.error("日志文件导出失败", e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                LOGGER.error("流关闭异常", e);
            }
        }
    }


    public static void main(String[] args) {
        write("梁梦林我喜欢你");
        write("哈哈哈哈哈哈");
    }
}
