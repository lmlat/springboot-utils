package com.github.akor.common;

import cn.hutool.core.io.IORuntimeException;
import org.apache.commons.io.FileUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @Company 11bee
 * @Author : AiTao
 * @Create : 2021/5/11 15:51
 * @Description : 自定义IO工具包
 * @Version: 1.0
 * 依赖包：commons-io
 */
@SuppressWarnings("all")
public class IOUtil {
    //默认缓冲区容量
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    //类路径标识
    private static final String CLASSPATH = "classpath:";

    private IOUtil() {
        throw new RuntimeException("拒绝反射实例化对象");
    }

    /**
     * 获取InputStream流
     *
     * @param clazz    类对象
     * @param filename 文件名
     * @return 输入流
     */
    public static InputStream getInputStream(Class<?> clazz, String filename) {
        return clazz.getClassLoader().getResourceAsStream(filename);
    }

    public static InputStream getInputStream(String filename) {
        return getInputStream(IOUtil.class, filename);
    }

    /**
     * 通过字节流转换String,得到InputStream
     *
     * @param str
     * @param charset
     * @return 输入流
     */
    public static InputStream toInputStream(String str, String charset) {
        try {
            return new ByteArrayInputStream(str.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream toInputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 转换Reader对象
     *
     * @param is 输入流
     * @return 返回Reader对象
     */
    public static Reader toReader(InputStream is) {
        return new InputStreamReader(is);
    }

    /**
     * 转换Writer对象
     *
     * @param os 输出流
     * @return 返回Writer对象
     */
    public static Writer toWriter(OutputStream os) {
        return new OutputStreamWriter(os);
    }


    /**
     * 转换字符串
     *
     * @param is 输入流
     * @return 字符串内容
     */
    public static String toString(InputStream is) {
        if (is == null) {
            return "";
        } else {
            StringBuilder ret = new StringBuilder();
            byte[] buff = new byte[DEFAULT_BUFFER_SIZE];
            try {
                for (int size; (size = is.read(buff)) != -1; ) {
                    ret.append(new String(buff, 0, size));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ret.toString();
        }
    }

    /**
     * 转换成字节数组
     *
     * @param is 输入流
     * @return 以字节序列返回数据
     */
    public static byte[] toBytes(InputStream is) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buff = new byte[DEFAULT_BUFFER_SIZE];
        try {
            for (int size = 0; (size = is.read(buff)) != 0; ) {
                bos.write(buff, 0, size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    /**
     * 将流转换成File文件
     *
     * @param is 输入流
     * @return {@link File}
     */
    public static File toFile(InputStream is) {
        byte[] buff = new byte[DEFAULT_BUFFER_SIZE];
        //创建临时文件
        File tempFile = new File(FileHelper.getTempDirectory(), UUID.randomUUID().toString().replaceAll("-", "") + ".tmp");
        if (tempFile != null && tempFile.exists()) {
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                for (int size; (size = is.read(buff)) != -1; ) {
                    fos.write(buff, 0, size);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tempFile;
    }

    /**
     * 序列化对象（保存在硬盘中）
     *
     * @param os  序列化输出路径
     * @param obj 序列化的对象信息
     * @return true序列化成功, 异常序列化失败
     */
    public static boolean serialize(OutputStream os,
                                    Object obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException("serialize失败!!!");
        }
        return true;
    }

    /**
     * 序列化对象（保存在内存中）
     *
     * @param obj 序列化的对象信息
     * @return 返回序列化后的字节序列
     */
    public static String serialize(Object obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException("serialize失败!!!");
        }
        return baos.toString();
    }

    /**
     * 对象反序列化（读取文件）
     *
     * @param is 反序列化输入流路径
     * @return 返回对象值
     */
    public static Object deserialize(InputStream is) {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("反序列化失败!!!");
        }
    }

    /**
     * 对象反序列化（读取内存数组）
     *
     * @param bytes 序列化后的字节序列值
     * @return 返回对象值
     */
    public static Object deserialize(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("反序列化失败!!!");
        }
    }

    /**
     * 压缩数据包
     *
     * @param data   需要压缩的字节数组
     * @param level  压缩级别
     * @param isOpen 是否要以GZIP或PKZIP格式压缩数据
     * @return 返回压缩后的字节数组
     */
    public static byte[] compress(byte[] data,
                                  int level,
                                  boolean isOpen) {
        //level = Deflater.BEST_COMPRESSION
        Deflater deflater = new Deflater(level, isOpen);
        //设置需要压缩的数据
        deflater.setInput(data);
        //完成
        deflater.finish();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int readCount = 0;
        while (!deflater.finished()) {
            readCount = deflater.deflate(buffer);
            if (readCount > 0) {
                baos.write(buffer, 0, readCount);
            }
        }
        deflater.end();
        return baos.toByteArray();
    }

    /**
     * 解压数据包
     *
     * @param data   需要解压的数据
     * @param isOpen 是否要以GZIP或PKZIP格式解压数据
     * @return 返回字节序列
     */
    public static byte[] decompress(byte[] data,
                                    boolean isOpen) {
        ByteArrayOutputStream baos = null;
        try {
            Inflater inflater = new Inflater(isOpen);
            inflater.setInput(data);
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int readCount = 0;
            while (!inflater.finished()) {
                readCount = inflater.inflate(buffer);
                if (readCount > 0) {
                    baos.write(buffer, 0, readCount);
                }
            }
            inflater.end();
        } catch (DataFormatException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    /**
     * 获取扫描器
     *
     * @return 返回Scanner对象
     */
    public static Scanner getScanner() {
        return new Scanner(System.in);
    }

    /**
     * 获取缓冲字符输入流
     *
     * @return 返回BufferedReader对象
     */
    public static BufferedReader scanner() {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    public static BufferedOutputStream toBuffered(OutputStream os) {
        if (os == null) {
            throw new NullPointerException();
        }
        return os instanceof BufferedOutputStream ? (BufferedOutputStream) os : new BufferedOutputStream(os);
    }

    public static OutputStream getOutputStream(File file) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(FileHelper.touch(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return toBuffered(out);
    }

    /**
     * 文件工具类
     */
    public static class FileHelper {
        public static File touch(File file) {
            if (null == file) {
                return null;
            } else {
                if (!file.exists()) {
                    File parentFile = file.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    try {
                        file.createNewFile();
                    } catch (Exception var2) {
                        throw new IORuntimeException(var2);
                    }
                }
                return file;
            }
        }

        //从文件夹中获取指定文件
        public static File getFile(File directory, String... names) {
            return FileUtils.getFile(directory, names);
        }

        //获取临时文件
        public static File getTempDirectory() {
            return new File(PathUtils.getTempPath());
        }

        //获取用户文件
        public static File getUserDirectory() {
            return new File(PathUtils.getUserHomePath());
        }

        //强制创建文件夹
        public static void mkdir(String path) {
            try {
                FileUtils.forceMkdir(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 文件复制(IO流)
         *
         * @param is 输入流
         * @param os 输出流
         * @return 返回文件大小
         */
        public static long copy(InputStream is, OutputStream os) {
            //缓冲区
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            //记录文件大小
            long count = 0;
            //记录一次读取的字节数
            int n = 0;
            try {
                while ((n = is.read(buffer)) != -1) {
                    os.write(buffer, 0, n);
                    count += n;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return count;
        }


        /**
         * 拷贝文件夹
         *
         * @param in  源文件夹
         * @param out 目录文件夹
         */
        public static void copyDirectory(File in, File out) {
            if (in == null) {
                System.err.println("源目录不存在");
                return;
            }
            if (!out.exists() && !out.mkdirs()) {
                System.out.println("源文件输出目录创建失败");
            }
            File[] files = in.listFiles();
            if (!Checks.isEmpty(files)) {
                for (File file : files) {
                    if (file.isFile()) {
                        copy(file, new File(out.getAbsolutePath() + File.separator + file.getName()));
                    }
                    if (file.isDirectory()) {
                        copyDirectory(new File(in.getAbsolutePath() + File.separator + file.getName()), new File(out.getAbsolutePath() + File.separator + file.getName()));
                    }
                }
            }
        }

        private static long copy(File in, File out) {
            try {
                return copy(new FileInputStream(in), new FileOutputStream(out));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return 0;
        }

        /**
         * 查看指定文件内容
         *
         * @param file 文件
         * @return 返回文件内容
         */
        public static String search(File file, String fileType) {
            StringBuilder content = new StringBuilder();
            switch (fileType.toLowerCase()) {
                case "txt":
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String str;
                        while ((str = reader.readLine()) != null) {
                            content.append(str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    throw new RuntimeException("不支持的文件类型!");
            }
            return content.toString();
        }

        /**
         * 文件复制(通道)
         *
         * @param in  输入管道
         * @param out 输出管道
         * @return 返回文件大小
         */
        public static long copy(FileChannel in,
                                FileChannel out) {
            long count = -1;
            try {
                //文件复制
                count = in.transferTo(0, in.size(), out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return count;
        }

        /**
         * 文件下载
         *
         * @param url 文件URL路径
         */
        public static void download(String url) {
            download(url, ""); //默认保存在类路径
        }

        public static void download(String url, String savePath) {
            download(url, savePath, "");
        }

        /**
         * 文件下载
         *
         * @param url      文件URL路径
         * @param savePath 文件保存路径
         */
        public static void download(String url, String savePath, String filename) {
            boolean isUrl = url.startsWith("http");
            if (!isUrl) {
                throw new RuntimeException("不支持下载本地文件!!!");
            }
            try (InputStream is = new URL(url).openStream()) {
                //截取文件名
                if (StringUtils.isBlank(filename) && isUrl) {
                    filename = url.substring(url.lastIndexOf("/") + 1);
                }
                //拼接保存路径
                savePath = StringUtils.isNotBlank(filename)
                        ? savePath + File.separator + filename //保存至指定路径
                        : PathUtils.getClassPath() + filename; //保存到类路径
                //REPLACE_EXISTING:替换现有文件（如果存在）
                //COPY_ATTRIBUTES:将属性复制到新文件
                //ATOMIC_MOVE:将文件作为原子文件系统操作移动
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                Files.copy(is, Paths.get(savePath), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("文件下载成功,已保存至:" + Paths.get(savePath).toFile().getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 判断文件是否存在
         *
         * @return true存在, false不存在
         */
        public static boolean exists(String path) {
            return Files.exists(Paths.get(checkClassPath(path)));
        }


        /**
         * 删除指定文件
         *
         * @param path 文件路径
         * @return true删除成功, false删除失败
         */
        public static boolean delete(String path) {
            try {
                Files.delete(Paths.get(checkClassPath(path)));
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        public static boolean deleteFileIfExists(File... files) {
            for (File file : files) {
                if (file.exists()) {
                    if (file.isDirectory() && !deleteDirIfExosts(file)) {
                        return false;
                    }
                    if (file.isFile() && !file.delete()) {
                        return false;
                    }
                } else {
                    System.out.println("文件不存在");
                }
            }
            return true;
        }

        public static boolean deleteDirIfExosts(File dir) {
            if (dir != null && dir.exists()) {
                File[] subFiles = dir.listFiles();
                if (subFiles != null && subFiles.length > 0) {
                    //删除目录下的子文件
                    for (File subFile : subFiles) {
                        if (!deleteDirIfExosts(subFile)) {
                            return false;
                        }
                    }
                }
            }
            //删除空目录
            return dir.delete();
        }

        /**
         * 文件移动
         *
         * @param oldPath 原路径,通过classpath前缀优先查找类路径下的文件
         * @param newPath 新路径
         * @param isCover 文件存在是否覆盖原文件
         * @return true成功, false失败
         */
        public static boolean move(String oldPath, String newPath, boolean isCover) {
            //优先查找类路径下文件
            oldPath = checkClassPath(oldPath);
            //文件不存在
            if (!exists(oldPath)) {
                throw new RuntimeException("文件 " + oldPath + " 不存在!");
            }
            File newFile = new File(newPath);
            //生成副本
            if (!isCover && newFile.exists()) {
                // 文件名 - 副本 (编号).扩展名
                List<String> data = StringUtils.split(newFile.getName(), ".");
                newPath = newPath.substring(0, newPath.indexOf(newFile.getName()));
                newPath += data.get(0) + " - 副本 (" + UUID.randomUUID().toString().replace("-", "") + ")." + data.get(1);
            }
            try (FileInputStream fis = new FileInputStream(oldPath);
                 FileOutputStream fos = new FileOutputStream(newPath)) {
                copy(fis, fos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //若在文件复制前没有关闭流，文件删除操作会失败
            return delete(oldPath);
        }
    }

    /**
     * 遍历文件树
     *
     * @param path 路径
     * @return 返回所有被遍历到的文件的绝对路径
     */
    public static Map<String, String> walk(String path) {
        return getFiles(new File(path), new HashMap<>());
    }

    /**
     * @param file
     * @return key:文件名, value:文件路径
     */
    private static Map<String, String> getFiles(File file, Map<String, String> filenameMap) {
        if (Objects.isNull(file)) {
            throw new NullPointerException("文件对象为空");
        }
        //获取文件夹下的所有文件
        File[] files = file.listFiles();
        if (!Objects.isNull(files) && files.length > 0) {
            for (File f : files) {
                System.out.println(f.getName());
                filenameMap.put(f.getName(), f.getAbsolutePath());
                //文件夹递归遍历
                if (f.isDirectory()) {
                    getFiles(f, filenameMap);
                }
            }
        }
        return filenameMap;
    }

    /**
     * 优先查找类路径下文件
     *
     * @param path 文件路径
     * @return 返回文件路径
     */
    private static String checkClassPath(String path) {
        if (path.startsWith(CLASSPATH)) {
            return PathUtils.getClassPath(path.substring(CLASSPATH.length()));
        }
        return path;
    }

    public static void main(String[] args) throws Exception {
        //1)获取指定文件夹下的文件
//        File file = FILE.getFile(new File(IOUtil.getClassPath()), "test.txt");
        //2)查看指定类型文件内容
//        System.out.println(IOUtil.FILE.search(file, FileType.TXT));
        //2)获取获取临时文件
//        File tmpFile = FileUtils.getTempDirectory();
        //3)获取临时文件路径
//        String tmpFilePath = FileUtils.getTempDirectoryPath();
//        System.out.println(tmpFilePath);//C:\Users\test\AppData\Local\Temp\
        //4)获取用户文件
//        File userFileDirectory = FileUtils.getUserDirectory();
        //5)获取用户文件路径
//        String userFileDirectoryPath = FileUtils.getUserDirectoryPath();
//        System.out.println(userFileDirectoryPath);//C:\Users\test


        //1.获取类路径
        /*System.out.println(IOUtil.getClassPath());
        System.out.println(IOUtil.getClassPath(false));
        System.out.println(IOUtil.getClassPath(true));*/
        //2.序列化和反序列化
        //2.1从文件读取对象
//        System.out.println(IOUtil.serialize(new User("lml", "345678")));
//        System.out.println(IOUtil.deserialize(new FileInputStream(new File(IOUtil.PATHS.getClassPath() + "user.txt"))));
        //2.2从字节数组流中读取对象
        //2.2.1第一种方式
        /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtil.serialize(baos, new User("aitao", "99999"));
        System.out.println("baos:" + baos);
        System.out.println(IOUtil.deserialize(new ByteArrayInputStream(baos.toByteArray())));*/
        //2.2.2第二种方式
        /*System.out.println(IOUtil.deserialize(IOUtil.serialize(new User("aitaotao", "11111"))));*/
        //字符串分割
        /*System.out.println(IOUtil.split("This is a  test, this is another test. I believe you can", ", "));*/
        //IO流----文件复制
        /*FileInputStream fis = new FileInputStream(new File(IOUtil.getPath("lml.jpg")));
        FileOutputStream fos = new FileOutputStream(new File(IOUtil.getClassPath() + "2.png"));*/
       /* long size = IOUtil.copy(fis, fos);
        System.out.println(size);*/
        /*FileChannel in = fis.getChannel();
        FileChannel out = fos.getChannel();
        System.out.println(IOUtil.copy(in, out));*/
        //文件下载
        /*IOUtil.download("https://i0.hdslb.com/bfs/sycp/creative_img/202104/e3bb0e0fc28e9b650dcd83ee5b0c8ce9.jpg");
        IOUtil.download("https://i0.hdslb.com/bfs/sycp/creative_img/202104/e3bb0e0fc28e9b650dcd83ee5b0c8ce9.jpg", "F:\\test");*/
        //判断文件/目录是否存在
        /*System.out.println(exists("F:\\test"));*/
        //文件删除
        /*System.out.println(delete("F:\\Project\\Project_JavaSe\\learn\\target\\classes\\e3bb0e0fc28e9b650dcd83ee5b0c8ce9.jpg"));*/
        //文件移动
        /*move("F:\\Project\\Project_JavaSe\\learn\\target\\classes\\1.png", "F:\\test\\lml.jpg", true);
        move("classpath:abcd.txt", "F:\\test\\lml.txt", true);*/
        //数据压缩
        /*byte[] compressData = IOUtil.compress("我爱你花木成畦手自栽花木成畦手自栽花木成畦手自栽基本面".getBytes(), Deflater.BEST_COMPRESSION, false);
        System.out.println(compressData.length);
        byte[] decompress = IOUtil.decompress(compressData, false);
        System.out.println(decompress.length);
        System.out.println(new String(decompress));*/
        //加载properties文件
        /*Properties pr = (Properties) IOUtil.load("info.properties", FileType.PROPERTIES);
        for (Object key : pr.keySet()) {
            System.out.println(pr.getProperty(key.toString()));
        }*/
        System.out.println(walk("src/main/java"));
//        Files.walk(Paths.get("src/main/java")).forEach(System.out::println);
        try (Stream<Path> paths =
                     Files.find(Paths.get("src/main/java"), Integer.MAX_VALUE,
                             (path, attributes) ->
                                     !attributes.isDirectory() && path.toString().contains("java"))) {
            paths.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = toFile(new FileInputStream(PathUtils.getClassPath("fdfs.conf")));
        System.out.println(file.getAbsolutePath());
    }

    public Stream<Path> filter(String path, String type) throws IOException {
        return Files.find(Paths.get("src/main/java"), Integer.MAX_VALUE, (p, attr) -> !attr.isDirectory() && p.toString().contains("java"));
    }
}
