package com.aitao.files;

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
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/09/15 12:38
 * @Description : Excel文件操作工具类
 */
public class ExcelUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * 导出Excel
     *
     * @param excelName excel文件名
     * @param srcData   导出的数据
     * @param clazz     导出实例类对象
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     */
    public static <T> boolean export(String excelName, List<T> srcData, Class<T> clazz,
                                     HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("export执行了: filename={},data={}", excelName, srcData);
        long start = System.currentTimeMillis();
        boolean isSuccess = true;
        OutputStream outputStream = null;
        try {
            setExcelExportFilename(generateFilename(excelName), request, response); // 设置导出文件名
            SXSSFWorkbook wb = new SXSSFWorkbook(500); // 创建工作簿
            SXSSFSheet sheet = wb.createSheet(excelName);
            CellStyle style = wb.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            style.setFont(font);
            // 记录所有标识ExcelExportEntity注解字段
            Map<String, Entity> fieldMap = new LinkedHashMap<>();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                ExcelExportReport eee = declaredField.getDeclaredAnnotation(ExcelExportReport.class);
                //未检测到ExcelExportReport注解的字段即为忽略导出字段
                if (eee != null) {
                    fieldMap.put(eee.property(), new Entity(eee.property(), eee.title(), eee.dateformat(), eee.percentage(), eee.decimal()));
                }
            }
            // 生成Sheet
            generateSheet(sheet, style, srcData, fieldMap);
            outputStream = response.getOutputStream();
            outputStream.flush();
            wb.write(outputStream);
        } catch (Exception e) {
            isSuccess = false;
            LOGGER.error("Excel文件导出失败", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    isSuccess = false;
                    LOGGER.error("流关闭异常", e);
                }
            }
            LOGGER.info("Excel文件导出成功, 耗时:{}ms", System.currentTimeMillis() - start);
        }
        return isSuccess;
    }

    /**
     * 设置导出文件名
     *
     * @param exportFilename 导出文件名（文件名+日期时间）
     * @param request        HttpServletRequest
     * @param response       HttpServletResponse
     * @throws UnsupportedEncodingException 不支持的编码异常
     */
    private static void setExcelExportFilename(String exportFilename, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        // 检查文件名中文乱码
        String filename = request.getHeader("User-Agent").toLowerCase().contains("firefox")
                ? new String(exportFilename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)
                : URLEncoder.encode(exportFilename, "UTF-8");
        // 设置默认文件名
        response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", (filename + ".xlsx")));
    }

    /**
     * 生成工作簿数据
     *
     * @param sheet    excel的工作表名称
     * @param style    单元格样式
     * @param srcData  数据源
     * @param fieldMap 属性集合（存储所有的属性名）
     */
    private static <T> void generateSheet(Sheet sheet, CellStyle style, List<T> srcData, Map<String, Entity> fieldMap) throws IllegalAccessException {
        // 第一行作标题行
        Row row = sheet.createRow(0);
        // 记录最大适应宽度
        Map<Integer, Integer> titleMaxWidth = new HashMap<>();
        // 生成标题单元格
        generateHeaderCell((SXSSFSheet) sheet, row, style, titleMaxWidth, fieldMap);
        // 生成普通单元格
        generateNormalCell((SXSSFSheet) sheet, style, srcData, titleMaxWidth, fieldMap);
    }

    /**
     * 生成普通单元格并填充数据
     *
     * @param sheet         工作簿
     * @param style         单元格样式
     * @param dataList      导出数据
     * @param titleMaxWidth 标题最大列宽
     * @param fieldMap      属性集合（存储所有的属性名）
     */
    public static <T> void generateNormalCell(SXSSFSheet sheet, CellStyle style, List<T> dataList, Map<Integer, Integer> titleMaxWidth, Map<String, Entity> fieldMap) throws IllegalAccessException {
        int currentColIndex;
        for (int i = 0; i < dataList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            T dataItem = dataList.get(i);
            currentColIndex = 0;
            for (Map.Entry<String, Entity> entry : fieldMap.entrySet()) {
                Entity value = entry.getValue();
                Cell cell = row.createCell(currentColIndex);
                //反射获取指定属性字段的值
                Object propertyValue = getValueByPropertyNameSeq(value.property, dataItem);
                if (propertyValue == null) {
                    cell.setCellValue("");
                } else if (propertyValue instanceof Date) {
                    cell.setCellValue(new SimpleDateFormat(value.dateformat).format((Date) propertyValue));
                } else if (value.decimal >= 0) {
                    cell.setCellValue(formatDecimal(basicTypeCast(propertyValue, double.class), value.decimal));
                } else if (value.percentage >= 0) {
                    cell.setCellValue(formatPercentage(basicTypeCast(propertyValue, double.class), value.percentage));
                } else {
                    cell.setCellValue(propertyValue.toString());
                }
                //限制单元格最大宽度为20000
                sheet.setColumnWidth(currentColIndex, Math.max(Math.min(cell.getStringCellValue().getBytes().length * 256 + 200, 20000), titleMaxWidth.get(currentColIndex)));
                currentColIndex += 1;
                cell.setCellStyle(style);
            }
        }
    }

    /**
     * 生成标题单元格并填充数据
     *
     * @param sheet         工作簿
     * @param row           行
     * @param style         单元格样式
     * @param titleMaxWidth 标题最大列宽
     * @param fieldMap      属性集合（存储所有的属性名）
     */
    public static void generateHeaderCell(SXSSFSheet sheet, Row row, CellStyle style, Map<Integer, Integer> titleMaxWidth, Map<String, Entity> fieldMap) {
        int index = 0;
        Cell cell;
        int adaptiveCellWidth; // 自适应单元格宽
        for (Map.Entry<String, Entity> entry : fieldMap.entrySet()) {
            cell = row.createCell(index);
            cell.setCellValue(entry.getValue().title);
            cell.setCellStyle(style);
            //自适应宽度 = 单元格内容长度 * 255 + 200
            adaptiveCellWidth = cell.getStringCellValue().getBytes().length * 256 + 200;
            sheet.setColumnWidth(index, adaptiveCellWidth);
            titleMaxWidth.put(index, adaptiveCellWidth); // 记录当前标题单元格的宽度
            index += 1;
        }
    }

    /**
     * 基础类型转换
     *
     * @param obj   Object类型数据
     * @param clazz 转换类型
     */
    private static <T> T basicTypeCast(Object obj, Class<T> clazz) {
        if (obj != null) {
            if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                return (T) new Integer(obj.toString());
            } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                return (T) new Long(obj.toString());
            } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                return (T) new Boolean(obj.toString());
            } else if (clazz.equals(Short.class) || clazz.equals(short.class)) {
                return (T) new Short(obj.toString());
            } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
                return (T) new Float(obj.toString());
            } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
                return (T) new Double(obj.toString());
            } else if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
                return (T) new Byte(obj.toString());
            } else if (clazz.equals(Character.class) || clazz.equals(char.class)) {
                return (T) new Character(obj.toString().charAt(0));
            }
        }
        return null;
    }

    /**
     * 随机生成文件名
     *
     * @param prefix 文件名前缀
     * @return 返回文件名
     */
    private static String generateFilename(String prefix) {
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
    private static Object getValueByPropertyName(String propertyName, Object obj) throws IllegalAccessException {
        Field field = getFieldByPropertyName(propertyName, obj.getClass());
        if (field == null) {
            LOGGER.error("{}类不存在字段名{}", obj.getClass().getSimpleName(), propertyName);
            throw new RuntimeException(obj.getClass() + "中不存在" + propertyName + "字段");
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
    private static Object getValueByPropertyNameSeq(String propertyName, Object obj) throws IllegalAccessException {
        String[] attributes = propertyName.split("\\.");
        //处理一个或多个属性的case,获取user.department.name中的name值
        return attributes.length == 1 ? getValueByPropertyName(propertyName, obj) : getValueByPropertyNameSeq(propertyName.substring(propertyName.indexOf(".") + 1), getValueByPropertyName(attributes[0], obj));
    }

    /**
     * 格式化百分比
     *
     * @param content 内容
     * @param bit     处理的数据 百分比数保留bit位小数， 20.00%
     */
    public static String formatPercentage(double content, int bit) {
        return new DecimalFormat(buildFormatter(bit).append("%").toString()).format(content);
    }

    /**
     * 格式化小数
     *
     * @param content 内容
     * @param bit     处理的数据 百分比数保留bit位小数， 20.00%
     */
    public static String formatDecimal(double content, int bit) {
        return new DecimalFormat(buildFormatter(bit).toString()).format(content);
    }

    /**
     * 构建格式化器
     */
    private static StringBuilder buildFormatter(int bit) {
        StringBuilder formatter = new StringBuilder("0");
        for (int i = 0; i < bit; i++) {
            formatter.append(i == 0 ? "." : "0");
        }
        return formatter;
    }

    static class Entity {
        //导出属性字段
        private final String property;
        //导出头部标题
        private final String title;
        //处理日期格式对象
        private final String dateformat;
        //百分比形式
        private final int percentage;
        //小数形式
        private final int decimal;

        public Entity(String property, String title, String dateformat, int percentage, int decimal) {
            this.property = property;
            this.title = title;
            this.dateformat = dateformat;
            this.percentage = percentage;
            this.decimal = decimal;
        }
    }
}


