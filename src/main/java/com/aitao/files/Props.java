package com.aitao.files;

import com.aitao.common.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author : AiTao
 * @Create : 2021-12-06 19:30
 * @Description : Properties文件操作工具类
 */
public class Props {
    private static final Logger LOGGER = LoggerFactory.getLogger(Props.class);

    private static final java.util.Properties prop = new java.util.Properties();

    private final String filename;

    private final List<String> paths;

    private final int limit;

    //文件加载是否成功
    private boolean isSuccess;

    private Props(PropertiesBuilder builder) {
        this.filename = builder.filename;
        this.paths = builder.paths;
        this.limit = builder.limit;
        this.isSuccess = true;
        for (boolean mark : builder.marks) {
            if (!mark) {
                this.isSuccess = false;
                break;
            }
        }
    }

    /**
     * 实例化PropertiesBuilder构建对象
     *
     * @return {@link PropertiesBuilder}
     */
    public static PropertiesBuilder create() {
        return new PropertiesBuilder();
    }

    public String getFilename() {
        return filename;
    }

    public List<String> getPaths() {
        return paths;
    }

    public int getLimit() {
        return limit;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * 获取属性值
     *
     * @param key 键
     * @return 查询的key对应的value值
     */
    private String getProperty(String key) {
        return prop.getProperty(key);
    }

    /**
     * 获取属性值
     *
     * @param key          键
     * @param defaultValue 默认返回值
     * @return 查询的key对应的value值
     */
    private String getProperty(String key, String defaultValue) {
        return prop.getProperty(key, defaultValue);
    }

    /**
     * 获取String类型值
     *
     * @param key 键
     * @return 查询的key对应的String类型value值
     */
    public String getString(String key) {
        return getProperty(key);
    }

    /**
     * 获取String类型值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 查询的key对应的String类型value值
     */
    public String getString(String key, String defaultValue) {
        return getProperty(key, defaultValue);
    }

    /**
     * 获取Integer类型值
     *
     * @param key 键
     * @return 查询的key对应的Integer类型value值
     */
    public Integer getInteger(String key) {
        String value = getProperty(key);
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            LOGGER.error(errorMsg("Integer", value), e);
        }
        return 0;
    }

    /**
     * 获取Integer类型值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 查询的key对应的Integer类型value值
     */
    public Integer getInteger(String key, int defaultValue) {
        Integer value = getInteger(key);
        return value == null ? defaultValue : value;
    }

    /**
     * 获取Boolean类型值
     *
     * @param key 键
     * @return 查询的key对应的Boolean类型value值
     */
    public Boolean getBoolean(String key) {
        String value = getProperty(key);
        if (value != null) {
            if (value.equalsIgnoreCase("true")) {
                return Boolean.TRUE;
            } else if (value.equalsIgnoreCase("false")) {
                return Boolean.FALSE;
            } else {
                LOGGER.warn(errorMsg("Boolean", value));
            }
        }
        return null;
    }

    /**
     * 获取Boolean类型值
     * 类型值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 查询的key对应的Boolean类型value值
     */
    public Boolean getBoolean(String key, Boolean defaultValue) {
        Boolean value = getBoolean(key);
        return value == null ? defaultValue : value;
    }

    /**
     * 获取Long类型值
     *
     * @param key 键
     * @return 查询的key对应的Long类型value值
     */
    public Long getLong(String key) {
        String value = getProperty(key);
        try {
            return Long.valueOf(value);
        } catch (Exception e) {
            LOGGER.error(errorMsg("Long", value), e);
        }
        return 0L;
    }

    /**
     * 错误日志
     */
    private String errorMsg(String type, String value) {
        return String.format("value is: '%s' Not a %s type and can not be converted to a %s", type, value, type);
    }

    /**
     * 获取Long类型值
     * 类型值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 查询的key对应的Long类型value值
     */
    public Long getLong(String key, Long defaultValue) {
        Long value = getLong(key);
        return value == null ? defaultValue : value;
    }

    static class PropertiesBuilder {
        private InputStream is;
        //文件名
        private String filename;
        //文件所在路径
        private final List<String> paths = new ArrayList<>();
        //文件是否已加载
        private boolean[] marks;
        //限制文件加载数量
        private int limit;

        public PropertiesBuilder() {
            this.setLimit(1).setFilename("default.properties");
        }

        public PropertiesBuilder setPath(String... paths) {
            Collections.addAll(this.paths, paths);
            return this;
        }

        public PropertiesBuilder setFilename(String filename) {
            this.filename = filename;
            return this;
        }

        public PropertiesBuilder setLimit(int limit) {
            this.limit = limit;
            this.marks = new boolean[limit];
            Arrays.fill(marks, true);
            return this;
        }

        /**
         * 构建属性构造器对象实例
         *
         * @return 返回PropertiesBuilder实例
         */
        public Props build() {
            return new Props(this);
        }

        /**
         * 加载properties文件
         *
         * @return 当前构建实例对象
         */
        public PropertiesBuilder load() {
            return load(filename);
        }

        /**
         * 加载properties文件（加载多个文件）
         *
         * @param paths 文件路径
         * @return 当前构建实例对象
         */
        public PropertiesBuilder load(String... paths) {
            setPath(paths);
            setFilename(String.join(",", paths));
            String path = "";
            int index = 0;
            try {
                for (int i = 0; i < limit; i++, index++) {
                    path = paths[i];
                    if (Paths.get(path).isAbsolute()) { //绝对地址
                        is = new FileInputStream(path);
                    } else if (path.startsWith("http") || path.startsWith("https")) { //超链接
                        is = new BufferedInputStream(new URL(path).openConnection().getInputStream());
                    } else { //相对地址
                        is = Props.class.getClassLoader().getResourceAsStream(path);
                    }
                    if (is == null) {
                        marks[index] = false;
                        throw new FileNotFoundException("类目录下未找到指定文件, filename: " + path);
                    }
                    prop.load(is);
                }
            } catch (IOException e) {
                marks[index] = false;
                LOGGER.error("加载文件失败, filename:{}", path, e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        marks[index] = false;
                        LOGGER.error("关闭流异常", e);
                    }
                }
            }
            return this;
        }
    }

    public static void main(String[] args) {
        Props propsBuilder = Props.create().setLimit(2).load(PathUtils.getClassPath("application.properties"), PathUtils.getClassPath("application2.properties")).build();
        System.out.println(propsBuilder.isSuccess());
        if (propsBuilder.isSuccess()) {
            System.out.println(propsBuilder.getPaths());
            System.out.println(propsBuilder.getString("spring.datasource.url"));
            System.out.println(propsBuilder.getBoolean("spring.datasource.url"));
            System.out.println(propsBuilder.getInteger("spring.datasource.url"));
            System.out.println(propsBuilder.getBoolean("spring.thymeleaf.cache"));
            System.out.println(propsBuilder.getString("map"));
            System.out.println(propsBuilder.getFilename());
            System.out.println(propsBuilder.getPaths());
        }
    }
}
