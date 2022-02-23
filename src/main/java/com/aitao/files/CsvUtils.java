package com.aitao.files;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author : AiTao
 * @Create : 2021-12-01 17:22
 * @Description : CSV文件操作工具类
 */
public class CsvUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvUtils.class);
    //字节顺序标记
    private static final byte[] BOM = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

    /**
     * 读取CSV文件
     *
     * @param is           输入流
     * @param isReadHeader 是否读取头部信息，true or false
     * @return 返回数据集合
     * @throws IOException
     */
    public static List<String[]> read(InputStream is, boolean isReadHeader) {
        long startTime = System.currentTimeMillis();
        try {
            List<String[]> retList = read0(new CsvReader(is, ',', StandardCharsets.UTF_8), isReadHeader);
            LOGGER.info("CSV文件录入成功, cost:{}ms", (System.currentTimeMillis() - startTime));
            return retList;
        } catch (IOException e) {
            LOGGER.error("CSV文件读取失败", e);
        }
        return new ArrayList<>();
    }

    /**
     * 读取CSV文件
     *
     * @param path         csv文件路径
     * @param isReadHeader 是否读取头部信息，true or false
     */
    public static List<String[]> read(String path, boolean isReadHeader) {
        long startTime = System.currentTimeMillis();
        try {
            File file = new File(path);
            //文件不存在 或者是 空文件
            if (!file.exists() || file.length() == 0) {
                return new ArrayList<>();
            }
            List<String[]> retList = read0(new CsvReader(path, ',', StandardCharsets.UTF_8), isReadHeader);
            LOGGER.info("CSV文件录入成功, cost:{}ms", (System.currentTimeMillis() - startTime));
            return retList;
        } catch (IOException e) {
            LOGGER.error("CSV文件读取失败", e);
        }
        return new ArrayList<>();
    }

    /**
     * 导出CSV文件
     *
     * @param os      输出流
     * @param headers 表头信息
     * @param data    内容数据
     * @return true成功，false失败
     */
    public static boolean write(OutputStream os, String[] headers, List<String[]> data) {
        long startTime = System.currentTimeMillis();
        CsvWriter csvWriter = null;
        boolean isSuccess = true;
        try {
            // 创建CSV写对象, new CsvWriter(文件路径，分隔符，编码格式);
            csvWriter = new CsvWriter(os, ',', StandardCharsets.UTF_8);
            if (headers != null && headers.length > 0) {
                csvWriter.writeRecord(headers);//写表头
            }
            for (String[] content : data) {
                csvWriter.writeRecord(content); //写内容
            }
            os.write(BOM); //在文件中增加字节顺序标记BOM，解决excel打开CSV文件时出现乱码
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            isSuccess = false;
            LOGGER.error("CSV文件导出失败", e);
        }
        LOGGER.info("CSV文件导出成功, cost:{}ms", (System.currentTimeMillis() - startTime));
        return isSuccess;
    }

    /**
     * CSV文件读取具体实现
     *
     * @param csvReader    csv读对象
     * @param isReadHeader 是否读取表头信息
     * @return 返回数据集合
     * @throws IOException
     */
    private static List<String[]> read0(CsvReader csvReader, boolean isReadHeader) throws IOException {
        List<String[]> retList = new ArrayList<>();
        // 忽略表头信息
        if (!isReadHeader) {
            csvReader.readHeaders();
        }
        // 一行一行读内容数据
        while (csvReader.readRecord()) {
            retList.add(csvReader.getValues());// 获取一行数据
        }
        csvReader.close();
        return retList;
    }


    public static void main(String[] args) {
        String[] headers = {"支付订单号", "商户订单号", "支付时间", "ssoid ", "imei", "订单金额", "退款金额", "支付渠道", "应用名称", "退款原因", "受理结果"};

        List<String[]> contents = new ArrayList<>();
        contents.add(new String[]{"RM2017091406320320FKYT0Y1A105688", "GC201709191358238200200000000", "2017/9/19  13:58:00 ", "123456", "86561312465", "5", "5", "现在支付", "汤姆猫跑酷", "掉单", "退款成功"});
        contents.add(new String[]{"RM2017091406320320FKYT0Y1A105688", "GC201709191358238200200000000", "2017/9/19  13:58:00 ", "123456", "86561312465", "5", "5", "现在支付", "汤姆猫跑酷", "掉单", "退款成功"});
        contents.add(new String[]{"RM2017091406320320FKYT0Y1A105688", "GC201709191358238200200000000", "2017/9/19  13:58:00 ", "123456", "86561312465", "5", "5", "现在支付", "汤姆猫跑酷", "掉单", "退款成功"});
        contents.add(new String[]{"RM2017091406320320FKYT0Y1A105688", "GC201709191358238200200000000", "2017/9/19  13:58:00 ", "123456", "86561312465", "5", "5", "现在支付", "汤姆猫跑酷", "掉单", "退款成功"});
        contents.add(new String[]{"RM2017091406320320FKYT0Y1A105688", "GC201709191358238200200000000", "2017/9/19  13:58:00 ", "123456", "86561312465", "5", "5", "现在支付", "汤姆猫跑酷", "掉单", "退款成功"});
        contents.add(new String[]{"RM2017091406320320FKYT0Y1A105688", "GC201709191358238200200000000", "2017/9/19  13:58:00 ", "123456", "86561312465", "5", "5", "现在支付", "汤姆猫跑酷", "掉单", "退款失败"});
        contents.add(new String[]{"RM2017091406320320FKYT0Y1A105688", "GC201709191358238200200000000", "2017/9/19  13:58:00 ", "123456", "86561312465", "5", "5", "现在支付", "汤姆猫跑酷", "掉单", "退款成功"});
        contents.add(new String[]{"RM2017091406320320FKYT0Y1A105688", "GC201709191358238200200000000", "2017/9/19  13:58:00 ", "123456", "86561312465", "5", "5", "现在支付", "汤姆猫跑酷", "掉单", "退款成功"});
        contents.add(new String[]{"RM2017091406320320FKYT0Y1A105688", "GC201709191358238200200000000", "2017/9/19  13:58:00 ", "123456", "86561312465", "5", "5", "现在支付", "汤姆猫跑酷", "掉单", "退款成功"});
        contents.add(new String[]{"RM2017091406320320FKYT0Y1A105688", "GC201709191358238200200000000", "2017/9/19  13:58:00 ", "123456", "86561312465", "5", "5", "现在支付", "汤姆猫跑酷", "掉单", "退款成功"});


        try {
            OutputStream out = new FileOutputStream("C:\\Users\\10034\\Desktop\\测试文件2.csv");
            CsvUtils.write(out, headers, contents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        try {
            List<String[]> list = CsvUtils.read(new FileInputStream("C:\\Users\\10034\\Desktop\\测试文件2.csv"), true);
            // 遍历读取的CSV文件
            for (String[] str : list) {
                System.out.println(Arrays.toString(str));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
