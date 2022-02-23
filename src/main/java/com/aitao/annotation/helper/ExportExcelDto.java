package com.aitao.annotation.helper;

import com.aitao.annotation.ExcelExportReport;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/09/15 12:38
 * @Description : ExcelExportEntity注解导出excel依赖类
 */
public class ExportExcelDto {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportExcelDto.class);

    /**
     * 导出Excel
     *
     * @param excelName excel文件名
     * @param srcData   导出的数据
     * @param clazz     导出实例类对象
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     */
    public static <T> void export(String excelName, List<T> srcData, Class<T> clazz, HttpServletRequest request, HttpServletResponse response) {
        try {
            //生成文件名
            excelName = generateFilename(excelName);
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            //处理文件名中文乱码
            String filename = request.getHeader("User-Agent").toLowerCase().contains("firefox")
                    ? new String(excelName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)
                    : URLEncoder.encode(excelName, "UTF-8");
            //设置默认文件名
            response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", filename + ".xlsx"));
            SXSSFWorkbook wb = new SXSSFWorkbook(500);
            SXSSFSheet sheet = wb.createSheet(excelName);
            CellStyle style = wb.createCellStyle();
            //设置边框
            style.setBorderBottom(BorderStyle.THIN); //下边框
            style.setBorderLeft(BorderStyle.THIN);//左边框
            style.setBorderTop(BorderStyle.THIN);//上边框
            style.setBorderRight(BorderStyle.THIN);//右边框
            style.setAlignment(HorizontalAlignment.CENTER);//设置水平居中
            style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            //设置字体
            Font font = wb.createFont();
            font.setFontName("宋体");
            style.setFont(font);
            //反射获取所有标识ExportEntityMap注解字段，并获取其中的属性值和属性表头部
            LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                ExcelExportReport declaredAnnotation = declaredField.getDeclaredAnnotation(ExcelExportReport.class);
                if (declaredAnnotation != null) {
                    fieldMap.put(declaredAnnotation.property(), declaredAnnotation.title());
                }
            }
            //生成工作簿
            generateSheet(sheet, srcData, fieldMap, style);
            OutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            LOGGER.error("导出Excel失败", e);
        }
    }

    /**
     * 生成工作簿数据
     *
     * @param sheet    excel的工作表名称
     * @param list     数据源
     * @param fieldMap Map<property, title>
     * @param style    单元格样式
     */
    private static <T> void generateSheet(Sheet sheet, List<T> list, Map<String, String> fieldMap, CellStyle style) throws Exception {
        String[] titleList = new String[fieldMap.size()], propertyList = new String[fieldMap.size()];
        AtomicInteger count = new AtomicInteger(0);
        fieldMap.forEach((key, value) -> {
            titleList[count.get()] = value;
            propertyList[count.get()] = key;
            count.incrementAndGet();
        });
        Row row = sheet.createRow(0);
        //存储最大列宽
        Map<Integer, Integer> maxWidth = new HashMap<>();
        //生成表头单元格并填充数据
        generateHeaderCell((SXSSFSheet) sheet, style, row, maxWidth, titleList);
        //生成单元格并填充数据
        generateNormalCell((SXSSFSheet) sheet, style, list, maxWidth, propertyList);
    }

    /**
     * 生成普通单元格并填充数据
     *
     * @param sheet        工作簿
     * @param style        单元格样式
     * @param dataList     导出数据
     * @param maxWidth     最大列宽
     * @param propertyList 属性集合（存储所有的属性名）
     */
    public static <T> void generateNormalCell(SXSSFSheet sheet, CellStyle style, List<T> dataList, Map<Integer, Integer> maxWidth, String[] propertyList) throws Exception {
        for (int i = 0; i < dataList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            T item = dataList.get(i);
            int j = 0;
            for (String property : propertyList) {
                Cell createCell = row.createCell(j);
                //获取指定属性字段的值
                Object propertyValue = getValueByPropertyNameSeq(property, item);
                createCell.setCellValue(propertyValue == null ? "" : propertyValue.toString());
                int width = createCell.getStringCellValue().getBytes().length * 256 + 200;
                //限制最大宽度
                if (width > 15000) width = 15000;
                maxWidth.put(j, Math.max(width, maxWidth.get(j)));
                createCell.setCellStyle(style);
                j++;
            }
        }
        for (int i = 0; i < propertyList.length; i++) {//自适应列宽
            sheet.setColumnWidth(i, maxWidth.get(i));
        }
    }

    /**
     * 生成标题单元格并填充数据
     *
     * @param sheet     工作簿
     * @param style     单元格样式
     * @param row       行
     * @param maxWidth  最大列宽
     * @param headTitle 头部标题信息
     */
    public static void generateHeaderCell(SXSSFSheet sheet, CellStyle style, Row row, Map<Integer, Integer> maxWidth, String[] headTitle) {
        for (int i = 0; i < headTitle.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headTitle[i]);
            cell.setCellStyle(style);
            int width = cell.getStringCellValue().getBytes().length * 256 + 200;
            sheet.setColumnWidth(i, width);
            maxWidth.put(i, width);//记录列宽
        }
    }

    /**
     * 随机生成文件名
     *
     * @param prefix 文件名前缀
     * @return 返回文件名
     */
    public static String generateFilename(String prefix) {
        if (prefix == null || prefix.length() == 0) {
            prefix = "Sheet1";
        }
        int randomNum = new Random().nextInt();
        Calendar calCurrent = Calendar.getInstance();
        return prefix + calCurrent.get(Calendar.YEAR) + (calCurrent.get(Calendar.MONTH) + 1) + calCurrent.get(Calendar.DATE) + calCurrent.get(Calendar.MILLISECOND) + (randomNum > 0 ? randomNum : randomNum * -1);
    }

    /**
     * 根据属性名获取字段Filed对象
     *
     * @param propertyName 属性名
     * @param clazz        包含该字段的类
     */
    private static Field getFieldByPropertyName(String propertyName, Class<?> clazz) {
        //获取类对象中的所有字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(propertyName)) {
                return field;
            }
        }
        //若当前类中没有,尝试找其父类中是否存在filed字段
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            return getFieldByPropertyName(propertyName, superClazz);
        }
        return null;
    }

    /**
     * 根据属性名获取字段的值
     *
     * @param propertyName 属性名
     * @param obj          对象
     */
    private static Object getValueByPropertyName(String propertyName, Object obj) throws Exception {
        Field field = getFieldByPropertyName(propertyName, obj.getClass());
        if (field == null) {
            LOGGER.error("{}类不存在字段名{}", obj.getClass().getSimpleName(), propertyName);
            throw new RuntimeException(obj.getClass().getSimpleName() + "类中不存在" + propertyName + "字段");
        }
        field.setAccessible(true);
        return field.get(obj);
    }

    /**
     * 根据属性名获取属性值
     *
     * @param propertyName 带路径的属性名或简单属性名
     * @param obj          对象
     */
    private static Object getValueByPropertyNameSeq(String propertyName, Object obj) throws Exception {
        String[] attributes = propertyName.split("\\.");
        //处理单个或多个的case
        return attributes.length == 1 ? getValueByPropertyName(propertyName, obj) : getValueByPropertyNameSeq(propertyName.substring(propertyName.indexOf(".") + 1), getValueByPropertyName(attributes[0], obj));
    }
}


