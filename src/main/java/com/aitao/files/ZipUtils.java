package com.aitao.files;

import com.aitao.common.Checks;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Company QAX
 * @Author : admin
 * @Create : 2022/1/15 20:31
 * @Description :
 */
public class ZipUtils {
    /**
     * 生成zip压缩文件
     *
     * @param path 生成路径
     * @throws IOException 操作流时，可能存在异常情况
     */
    public static void write(Path path) throws IOException {
        File file = path.toFile();
        // 压缩文件的路径不存在
        if (!file.exists()) {
            throw new RuntimeException("路径 " + path + " 文件不存在，无法进行压缩...");
        }
        // 目的压缩文件
        String generateFileName = file.getParent() + File.separator + file.getName() + ".zip";
        // 压缩输出流
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(generateFileName)));
        compress0(zos, file, "");
        zos.close();
        System.out.printf("源文件路径:%s,目标压缩文件路径:%s\n", file, generateFileName);
    }

    /**
     * 生成zip压缩文件
     *
     * @param filepath 压缩文件路径
     * @throws IOException 操作流时，可能存在异常情况
     */
    public static void write(CharSequence filepath) throws IOException {
        if (Checks.isEmpty(filepath)) {
            throw new RuntimeException(String.format("找不到指定的文件:%s", filepath));
        }
        write(Paths.get(filepath.toString()));
    }

    /**
     * 压缩文件
     *
     * @param out    输出流
     * @param file   目标文件
     * @param subdir 文件夹
     * @throws IOException
     */
    private static void compress0(ZipOutputStream out, File file, String subdir) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                //将文件夹添加到下一级打包目录
                out.putNextEntry(new ZipEntry(subdir + File.separator));
                if (!subdir.isEmpty()) {
                    subdir += File.separator;
                }
                for (File value : files) {
                    compress0(out, value, subdir + value.getName());
                }
            }
        } else {
            FileInputStream is = new FileInputStream(file);
            if (subdir.isEmpty()) {
                subdir = file.getName();
            }
            // 标记要打包的文件
            out.putNextEntry(new ZipEntry(subdir));
            int size;
            byte[] bytes = new byte[8092];
            while ((size = is.read(bytes)) != -1) {
                out.write(bytes, 0, size);
            }
            is.close();
        }
    }
}
