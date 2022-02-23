package com.github.akor.beans;

import org.apache.tika.Tika;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * 基于tika的文件类型检测工具类
 * 根据MimeType来检测文件类型
 *
 * @Company QAX
 * @Author : admin
 * @Create : 2022/1/26 10:46
 */
public class FileTypeUtils {
    public static String getMimeType(File file) {
        if (file.isDirectory()) {
            return "The target is a directory";
        }
        AutoDetectParser parser = new AutoDetectParser();
        parser.setParsers(new HashMap<MediaType, Parser>());
        Metadata metadata = new Metadata();
        try (InputStream stream = new FileInputStream(file)) {
            parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return metadata.get(HttpHeaders.CONTENT_TYPE);
    }

    public static String getType(File file) throws IOException {
        return new Tika().detect(file);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(getMimeType(new File("C:\\Users\\10034\\Desktop\\Learn\\springboot-utils\\src\\main\\resources\\static\\jquery-min-3.4.1.js")));
        System.out.println(getType(new File("C:\\Users\\10034\\Desktop\\Learn\\springboot-utils\\src\\main\\resources\\static\\jquery-min-3.4.1.js")));
        System.out.println(getType(new File("C:\\Users\\10034\\Desktop\\Learn\\springboot-utils\\src\\main\\java\\com\\aitao\\SpringbootUtilsApplication.java")));
    }
}
