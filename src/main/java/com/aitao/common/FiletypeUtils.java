package com.aitao.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
/**
 * @Author : AiTao
 * @Create : 2021-11-01 23:29
 * @Description : 文件类型检测工具类
 */
public class FiletypeUtils {
    public final static Map<String, String> FILE_TYPE_MAP = new HashMap<>();

    static {
        //key:文件类型名，value:文件类型十六进制串前缀序列
        FILE_TYPE_MAP.put("jpg", "FFD8FF"); //JPEG (jpg)
        FILE_TYPE_MAP.put("png", "89504E47");  //PNG (png)
        FILE_TYPE_MAP.put("gif", "47494638");  //GIF (gif)
        FILE_TYPE_MAP.put("tif", "49492A00");  //TIFF (tif)
        FILE_TYPE_MAP.put("bmp", "424D"); //Windows Bitmap (bmp)
        FILE_TYPE_MAP.put("dwg", "41433130"); //CAD (dwg)
        FILE_TYPE_MAP.put("html", "68746D6C3E");  //HTML (html)
        FILE_TYPE_MAP.put("rtf", "7B5C727466");  //Rich Text Format (rtf)
        FILE_TYPE_MAP.put("xml", "3C3F786D6C");
        FILE_TYPE_MAP.put("zip", "504B0304");
        FILE_TYPE_MAP.put("rar", "52617221");
        FILE_TYPE_MAP.put("psd", "38425053");  //Photoshop (psd)
        FILE_TYPE_MAP.put("eml", "44656C69766572792D646174653A");  //Email [thorough only] (eml)
        FILE_TYPE_MAP.put("dbx", "CFAD12FEC5FD746F");  //Outlook Express (dbx)
        FILE_TYPE_MAP.put("pst", "2142444E");  //Outlook (pst)
        FILE_TYPE_MAP.put("xls", "D0CF11E0");  //MS Word
        FILE_TYPE_MAP.put("doc", "D0CF11E0");  //MS Excel 注意：word 和 excel的文件头一样
        FILE_TYPE_MAP.put("mdb", "5374616E64617264204A");  //MS Access (mdb)
        FILE_TYPE_MAP.put("wpd", "FF575043"); //WordPerfect (wpd)
        FILE_TYPE_MAP.put("eps", "252150532D41646F6265");
        FILE_TYPE_MAP.put("ps", "252150532D41646F6265");
        FILE_TYPE_MAP.put("pdf", "255044462D312E");  //Adobe Acrobat (pdf)
        FILE_TYPE_MAP.put("qdf", "AC9EBD8F");  //Quicken (qdf)
        FILE_TYPE_MAP.put("pwl", "E3828596");  //Windows Password (pwl)
        FILE_TYPE_MAP.put("wav", "57415645");  //Wave (wav)
        FILE_TYPE_MAP.put("avi", "41564920");
        FILE_TYPE_MAP.put("ram", "2E7261FD");  //Real Audio (ram)
        FILE_TYPE_MAP.put("rm", "2E524D46");  //Real Media (rm)
        FILE_TYPE_MAP.put("mpg", "000001BA");  //
        FILE_TYPE_MAP.put("mov", "6D6F6F76");  //Quicktime (mov)
        FILE_TYPE_MAP.put("asf", "3026B2758E66CF11"); //Windows Media (asf)
        FILE_TYPE_MAP.put("mid", "4D546864");  //MIDI (mid)
    }


    /**
     * 获取文件类型,包括图片,若格式不是已配置的,则返回null
     *
     * @param file 文件源
     * @return 文件类型
     */
    public static String getFileType(File file) {
        byte[] b = new byte[50];
        if (file != null && file.exists() && file.isFile()) {
            try (InputStream is = new FileInputStream(file)) {
                is.read(b);
            } catch (IOException e) {
                throw new RuntimeException("无法识别此文件类型，path: " + file.getAbsolutePath());
            }
        }
        return getFileTypeByStream(b);
    }

    /**
     * 按流获取文件类型
     *
     * @param b 文件字节序列
     * @return 文件类型
     */
    private static String getFileTypeByStream(byte[] b) {
        String fileTypeHex = getFileHexString(b);
        System.out.println(fileTypeHex);
        for (Map.Entry<String, String> entry : FILE_TYPE_MAP.entrySet()) {
            if (fileTypeHex.toUpperCase().startsWith(entry.getValue())) {
                return entry.getKey();
            }
        }
        return fileTypeHex;
    }

    /**
     * 获取文件类型十六进制字符串
     *
     * @param b 文件类型的字节序列
     * @return 文件类型的十六进制串
     */
    private static String getFileHexString(byte[] b) {
        if (b == null || b.length <= 0) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (byte value : b) {
            String hv = Integer.toHexString(value & 0xFF);
            stringBuilder.append(hv.length() < 2 ? 0 : hv);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        System.out.println("23e8bf9ee68ea5e8b685e697b620327300636f6e6e6563745f74696d656f7574203d20320023e7bd91e7bb9ce8b685e6".length());
        System.out.println(getFileType(new File("C:\\Users\\10034\\Desktop\\Learn\\springboot-utils\\src\\main\\resources\\fdfs.conf")));
    }
}
