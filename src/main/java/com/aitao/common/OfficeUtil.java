package com.aitao.common;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @Author : AiTao
 * @Create : 2021-10-24 20:47
 * @Description : 基于spire工具包的office文件工具类
 */
public class OfficeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfficeUtil.class);

    public static void pdfToWord() {
        try {
            String pdfFile = "C:\\Users\\10034\\Desktop\\Learn\\springboot-utils\\target\\classes\\高三数学试题.pdf";
            PDDocument doc = PDDocument.load(new File(pdfFile));
            int pagenumber = doc.getNumberOfPages();
            pdfFile = pdfFile.substring(0, pdfFile.lastIndexOf("."));
            String fileName = pdfFile + ".doc";
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(fileName);
            Writer writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);// 排序
            stripper.setStartPage(1);// 设置转换的开始页
            stripper.setEndPage(pagenumber);// 设置转换的结束页
            stripper.writeText(doc, writer);
            writer.close();
            doc.close();
            System.out.println("pdf转换word成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将多页pdf转化为多张图片
     *
     * @param pdfPath 表示pdf的路径
     * @return 转化后的图片的路径集合
     * @throws IOException
     */
    public static List<String> pdfToImages(String pdfPath) throws IOException {
        LOGGER.info("将多页pdf转化为图片，pdf路径为：" + pdfPath);
        File pdfFile = new File(pdfPath);
        //加载pdf文件
        PDDocument pdDocument = PDDocument.load(pdfFile);
        //获取总页数
        int pageCount = pdDocument.getNumberOfPages();
        PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
        List<String> imagePathList = new ArrayList<>();
        String fileParent = pdfFile.getParent();
        for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
            String newDirectory = fileParent + File.separator + pdfFile.getName().split("\\.")[0];
            File file = new File(newDirectory);
            if (!file.exists()) {
                file.mkdirs();
            }
            String imgPath = newDirectory + File.separator + (pageIndex + 1) + ".png";
            BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, 105, ImageType.RGB);
            ImageIO.write(image, "png", new File(imgPath));
            imagePathList.add(imgPath);
            LOGGER.info("第{}张生成的图片路径为：{}", pageIndex, imgPath);
        }
        pdDocument.close();
        return imagePathList;
    }

    public static void main(String[] args) throws IOException {
        List<String> list = new ArrayList<>();
        list.add("aitao");
        list.add("lml");
        ListIterator<String> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            System.out.println(listIterator.nextIndex() + " " + listIterator.next());
        }
//        System.out.println(pdfToImages("C:\\Users\\10034\\Desktop\\Learn\\springboot-utils\\target\\classes\\高三数学试题.pdf"));
    }
}
