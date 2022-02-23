package com.github.akor.files;

import com.github.akor.common.DateUtils;
import com.github.akor.model.User;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author : AiTao
 * @Create : 2021-12-02 19:01
 * @Description :
 */
public class PdfUtils {
    /**
     * @param objectList    报表内容list
     * @param copyPath      生成文件路径（至文件名）
     * @param title         报表标题
     * @param columnHeaders 报表 列标题数组
     * @param <T>           内容类型
     */
    public static <T> void export(List<T> objectList, String copyPath, String title, String[] columnHeaders) {
        try {
            // 新建文件
            Document document = new Document();
            //建立一个书写器(Writer)与document对象关联，并把文档写入到磁盘中
            PdfWriter.getInstance(document, new FileOutputStream(copyPath));
            document.open();
            // 中文字体
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            // 标题字体
            Font titleChinese = new Font(bfChinese, 16, Font.BOLD);
            // 内容小标题字体
            Font contenttitleChinese = new Font(bfChinese, 11, Font.BOLD);
            //设置标题
            Paragraph par = new Paragraph(title, titleChinese);
            par.setAlignment(Element.ALIGN_CENTER);
            //文档中加入标题
            document.add(par);
            //文档中加入空行
            Paragraph blankRow51 = new Paragraph(18f, " ", contenttitleChinese);
            document.add(blankRow51);
            //设置表格的列数
            int col = columnHeaders.length;
            //新建一个表格
            PdfPTable table = new PdfPTable(col);
            //设置表格占PDF文档100%宽度
            table.setWidthPercentage(100);
            //设置每列表格宽度
            int[] widths = new int[columnHeaders.length];
            int wight = 100 / columnHeaders.length;
            Arrays.fill(widths, wight);
            table.setWidths(widths);
            //设置表格标题样式
            BaseColor lightGrey01 = new BaseColor(0xCC, 0xCC, 0xCC);
            for (String columnHeader : columnHeaders) {
                PdfPCell cell = toPdfPCell(columnHeader, Element.ALIGN_CENTER);
                cell.setBackgroundColor(lightGrey01);
                table.addCell(cell);
            }
            //表格赋值啊，这里使用反射获取对象属性
            if (objectList.size() > 0) {
                for (T obj : objectList) {
                    Field[] fields = obj.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        table.addCell(toPdfPCell(field.get(obj).toString(), Element.ALIGN_CENTER));
                    }
                }
            }
            //把表格加入到文档中
            document.add(table);
            //关闭文档
            document.close();
        } catch (DocumentException | IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static PdfPCell toPdfPCell(String name, int align) throws DocumentException, IOException {
        // 中文字体
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        // 内容字体
        Font fontChinese = new Font(bfChinese, 11, Font.NORMAL);
        PdfPCell cell = new PdfPCell(new Paragraph(name, fontChinese));
        // 设置内容水平居中显示
        cell.setHorizontalAlignment(align);
        // 设置垂直居中
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    /**
     * 给PDF文件加水印
     *
     * @param inputFile  操作的文件位置
     * @param outputFile 生成的文件位置
     * @param date       水印内容
     * @param user       水印内容
     */
    public static void waterMark(String inputFile, String outputFile, String date, String user) {
        try {
            PdfReader reader = new PdfReader(inputFile);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFile));

            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            Rectangle pageRect = null;
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.3f);
            gs.setStrokeOpacity(0.4f);
            int total = reader.getNumberOfPages() + 1;

            JLabel label = new JLabel();
            FontMetrics metrics;
            int textH = 0;
            int textW = 0;
            label.setText(date);
            metrics = label.getFontMetrics(label.getFont());
            textH = metrics.getHeight();
            textW = metrics.stringWidth(label.getText());

            PdfContentByte under;
            for (int i = 1; i < total; i++) {
                under = stamper.getOverContent(i);
                under.saveState();
                under.setGState(gs);
                under.beginText();
                under.setFontAndSize(base, 18);
                //水印位置坐标
                int dateHeight = 20;
                int dateWidth = 520;
                int userHeight = 40;
                int userWidth = 560;
                // 水印文字成0度角倾斜
                under.showTextAligned(Element.ALIGN_LEFT, user, userWidth - textW,
                        userHeight - textH, 0);
                under.showTextAligned(Element.ALIGN_LEFT, date, dateWidth - textW,
                        dateHeight - textH, 0);
                under.endText();
            }
            //关闭流
            stamper.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 进行PDF文件下载
     *
     * @param filename       服务器文件
     * @param exportFileName 下载到客户端的文件名
     * @param response
     */
    public void download(String filename, String exportFileName, HttpServletResponse response) {
        InputStream input = null;
        OutputStream out = null;
        try {
            input = new FileInputStream(filename);
            response.reset();
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment;filename=\"" + new String(exportFileName.getBytes(StandardCharsets.UTF_8), "ISO_8859_1") + "\"");
            response.addHeader("Content-Length", "" + input.available());
            out = response.getOutputStream();
            IOUtils.copy(input, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(out);
        }
    }

    public static void main(String[] args) {
        List<User> list = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            User user = new User();
            user.setAge((String.valueOf(i + 10)));
            user.setUsername("aitao" + i);
            user.setPaaswod("123456" + i);
            list.add(user);
        }
        String title = "用户信息表";
        //这里需要保持标题数组的顺序和Student中定义的顺序一致
        String[] columnHeaders = {"用户名", "密码", "年龄"};
        String path = "C:\\Users\\10034\\Desktop\\Learn\\springboot-utils\\target\\classes\\User.pdf";
        //生成PDF文件，不带水印
//        export(list, path, title, columnHeaders);
        //给生成的PDF文件加水印（时间+用户）
        waterMark(path, "C:\\Users\\10034\\Desktop\\Learn\\springboot-utils\\target\\classes\\User1.pdf", DateUtils.toString(new Date()), "艾韬");
    }
}
