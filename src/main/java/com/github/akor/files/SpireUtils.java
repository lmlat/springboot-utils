package com.github.akor.files;

import com.github.akor.common.PathUtils;
import com.spire.doc.Document;
import com.spire.pdf.PdfDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @Author : AiTao
 * @Create : 2021-12-02 19:18
 * @Description : 基于spire工具操作office文件
 */
public class SpireUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(SpireUtils.class);


    /**
     * @param filename
     * @param savePath
     */
    public static void toPdf(String filename, String savePath) {
        try {
            //实例化Document类的对象
            Document doc = new Document();
            //加载Word
            doc.loadFromFile(URLDecoder.decode(PathUtils.getClassPath(filename), "utf-8"));
            //保存为PDF格式
            doc.saveToFile(savePath, com.spire.doc.FileFormat.PDF);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void toWord(String filename, String savePath) {
        try {
            PdfDocument pdf = new PdfDocument();
            pdf.loadFromFile(URLDecoder.decode(PathUtils.getClassPath(filename), "utf-8"));
            pdf.saveToFile(savePath, com.spire.pdf.FileFormat.DOCX);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        toPdf("高三数学试题.docx", "高三数学试题.pdf");
    }
}
