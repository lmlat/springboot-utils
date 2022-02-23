package com.aitao.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/07/23 15:58
 * @Description : 路径工具类
 */
public class PathUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PathUtils.class);
    //用户的主目录
    private static final String DEFAULT_USER_HOME = "user.home";
    //用户当前的工作目录
    private static final String DEFAULT_USER_DIR = "user.dir";
    //默认临时文件路径
    private static final String DEFAULT_TMP_DIR = "java.io.tmpdir";
    //一个或多个扩展目录的路径
    private static final String DEFAULT_JAVA_EXT_DIRS = "java.ext.dirs";
    //路径分隔符, 在windows下是 \ ，在Linux下是 /
    private static final String DEFAULT_PATH_SEPARATOR = "path.separator";
    //行分隔符号, 在windows下是 \r\n，在Linux下面是 \n， 在Mac下是 \r
    private static final String DEFAULT_LINE_SEPARATOR = "line.separator";
    //java安装目录
    private static final String DEFAULT_JAVA_HOME = "java.home";
    //用户账户名称
    private static final String DEFAULT_USER_NAME = "user.name";
    //文件分隔符
    private static final String DEFAULT_FILE_SEPARATOR = "file.separator";
    //java运行时环境版本
    private static final String DEFAULT_JAVA_VERSION = "java.version";
    //java运行时环境供应商
    private static final String DEFAULT_JAVA_VENDOR = "java.vendor";
    //java供应商的url
    private static final String DEFAULT_JAVA_VENDOR_URL = "java.vendor.url";
    //操作系统版本
    private static final String DEFAULT_OS_VERSION = "os.version";
    //操作系统的名称
    private static final String DEFAULT_OS_NAME = "os.name";
    //操作系统的架构
    private static final String DEFAULT_OS_ARCH = "os.arch";
    //要使用的JIT编译器的名称
    private static final String DEFAULT_JAVA_COMPILER = "java.compiler";
    //java类路径
    private static final String DEFAULT_JAVA_CLASS_PATH = "java.class.path";
    //java类格式版本号
    private static final String DEFAULT_JAVA_CLASS_VERSION = "java.class.version";
    //加载库时搜索的路径列表
    private static final String DEFAULT_JAVA_LIBRARY_PATH = "java.library.path";
    //java运行时环境规范版本
    private static final String DEFAULT_SPECIFICATION_VERSION = "java.specification.version";
    //java运行时环境规范供应商
    private static final String DEFAULT_SPECIFICATION_VENDOR = "java.specification.vendor";
    //java运行时环境规范名称
    private static final String DEFAULT_SPECIFICATION_NAME = "java.specification.name";
    private static String diskPaths;
    private static String pathRules;

    /**
     * 是否为绝对路径
     *
     * @param path 路径
     * @return true or false
     */
    public static boolean isAbsolute(String path) {
        return Checks.isNotEmpty(path) && Paths.get(path).isAbsolute();
    }

    /**
     * 是否为相对路径
     *
     * @param path 路径
     * @return true or false
     */
    public static boolean isRelative(String path) {
        return !isAbsolute(path);
    }

    /**
     * 获取classes目录下的指定文件路径
     *
     * @param filename 文件名
     * @return 返回指定文件的路径
     */
    public static String getClassPath(String filename) {
        Paths.get("");
        URL resource = PathUtils.class.getClassLoader().getResource(filename);
        if (Objects.isNull(resource)) {
            LOGGER.error("类路径下不存在 {} 文件", filename);
            throw new RuntimeException("类路径下不存在 " + filename + " 文件");
        }
        return formatPath(resource.getPath());
    }

    /**
     * 获取指定类对象所在的文件路径
     *
     * @param clazz 类对象
     * @return 返回指定类所在的路径
     */
    public static String getClassPath(Class<?> clazz) {
        URL resource = clazz.getResource("");
        if (Objects.isNull(resource)) {
            LOGGER.error("类目录中不存在 {} 类文件", clazz.getName());
            throw new RuntimeException("类路径中不存在 " + clazz.getName() + " 类文件");
        }
        return formatPath(resource.getPath());
    }

    /**
     * 获取类目录路径
     *
     * @return String
     */
    public static String getClassPath() {
        return getClassPath("");
    }

    /**
     * 获取项目工程路径
     *
     * @return String
     */
    public static String getProjectPath() {
        return toLinuxSeparator(System.getProperty(DEFAULT_USER_DIR));
    }

    /**
     * 获取临时文件夹路径
     *
     * @return String
     */
    public static String getTempPath() {
        return toLinuxSeparator(System.getProperty(DEFAULT_TMP_DIR));
    }

    /**
     * 获取用户文件夹路径
     *
     * @return String
     */
    public static String getUserHomePath() {
        return toLinuxSeparator(System.getProperty(DEFAULT_USER_HOME));
    }

    /**
     * 格式化
     *
     * @param path 路径字符串
     * @return 路径
     */
    private static String formatPath(String path) {
        return new String(path.toCharArray(), 1, path.length() - 1);
    }

    /**
     * 转换成Linux风格的文件分隔符
     *
     * @param path 路径
     * @return 新路径
     */
    private static String toLinuxSeparator(String path) {
        return path.replaceAll("\\\\", "/");
    }

    // 保存要匹配的文件名
    private static String filename = "application.properties";

    //最终匹配结果集合,考虑到可能会出现同名的文件夹和文件
    private static final List<File> resultList = new ArrayList<>();

    //路径扫描优先级（优先扫描指定的文件夹）
    private static File[] scanPriority = {new File("C:\\Users\\10034\\Desktop\\Learn\\springboot-utils\\src\\main\\resources"), new File("D:\\安装包")};

    //扫描文件路径过路规则
    private static String filter = File.separator;

    public static void setScanPriority(String[] paths) {
        if (paths != null && paths.length >= 1) {//过滤非法数据
            scanPriority = Arrays.stream(paths).filter(e -> e != null && !e.isEmpty() && !e.contains(filter)).map(String::trim).map(File::new).toArray(File[]::new);
        }
    }

    public File[] getScanPriority() {
        return scanPriority;
    }

    public String getFilter() {
        return filter;
    }

    public static void setFilter(String filter) {
        PathUtils.filter = filter;
    }

    /**
     * 搜索指定文件，最终匹配结果集合,考虑到可能会出现同名的文件夹和文件
     *
     * @param targetFile 目标文件
     * @param diskPath   搜索路径
     * @return 文件匹配结果集
     */
    public static List<File> search(String targetFile, String diskPath) {
        filename = targetFile;
        // 扫描所有盘
        if (diskPath == null || diskPath.trim().length() == 0) {
            if (scanPriority == null || scanPriority.length == 0) {
                scanPriority = File.listRoots();
            }
            //扫描文件
            for (File file : scanPriority) {
                System.out.println("开始扫描文件：" + file.getAbsolutePath());
                scan(file);
            }
            System.out.println("扫描完成");
        } else { //扫描指定路径
            File checkFile = new File(diskPath);
            if (!checkFile.isDirectory()) {
                return null;
            }
            scan(checkFile);
            scanPriority[0] = checkFile;
        }
        return resultList;
    }

    /**
     * 递归扫描文件夹下的所有文件
     *
     * @param file 文件夹
     */
    private static void scan(File file) {
        if (file == null) {
            return;
        }
        File[] subFiles = file.listFiles();
        if (subFiles != null && subFiles.length > 0) {
            for (File subFile : subFiles) {
                //匹配成功，加入结果集
                if (subFile != null) {
                    if (filename.equals(subFile.getName())) {
                        resultList.add(subFile);
                    }
                    if (subFile.isDirectory()) { //扫描子文件
                        scan(subFile);
                    }
                }
            }
        }
    }
}
