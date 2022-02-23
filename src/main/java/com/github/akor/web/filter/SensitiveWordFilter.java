package com.github.akor.web.filter;

import com.github.akor.common.PathUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author : AiTao
 * @Create : 2021-12-01 19:54
 * @Description : 敏感词过滤器
 */
public class SensitiveWordFilter {
    public static String DEFAULT_SENSITIVE_WORDS_PATH = "keyword.txt";

    private static Set<String> sensitiveWords = null;

    private enum SingleTon {
        INSTANCE;
        private final SensitiveWordFilter sensitiveWordFilter;

        //私有化枚举的构造函数
        SingleTon() {
            sensitiveWordFilter = new SensitiveWordFilter();
        }

        public SensitiveWordFilter getInstance() {
            return sensitiveWordFilter;
        }
    }

    private SensitiveWordFilter() {
        try {
            sensitiveWords = loadKeywordFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取敏感词对象实例
     */
    public static SensitiveWordFilter getInstance() {
        return SingleTon.INSTANCE.getInstance();
    }

    /**
     * 加载敏感词文件数据
     *
     * @return 敏感词集合
     * @throws IOException
     */
    public static Set<String> loadKeywordFile() throws IOException {
        Set<String> sensitiveWords = new HashSet<>();
        String line;
        BufferedReader reader;
        System.out.println(PathUtils.getClassPath(DEFAULT_SENSITIVE_WORDS_PATH));
        InputStream is = SensitiveWordFilter.class.getClassLoader().getResourceAsStream(DEFAULT_SENSITIVE_WORDS_PATH);
        if (is != null) {
            reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            if (reader != null) {
                while ((line = reader.readLine()) != null) {
                    if (line != null && !line.trim().isEmpty()) {
                        sensitiveWords.add(line);
                    }
                }
            }
        }
        return sensitiveWords;
    }

    /**
     * 获取敏感词数据集合
     *
     * @return 返回敏感词集合
     */
    public Set<String> get() {
        return sensitiveWords;
    }

    /**
     * 过滤敏感词
     *
     * @param data 过滤数据
     * @return 返回处理后的数据
     */
    public String filter(String data) {
        Set<String> sensitiveWords = SensitiveWordFilter.getInstance().get();
        if (data != null) {
            for (String word : sensitiveWords) {
                if (data.contains(word)) {
                    data = data.replaceAll(word, "**");
                }
            }
        }
        return data;
    }


    public static void main(String[] args) {
        System.out.println(SensitiveWordFilter.getInstance().get().size());
        String data = "测试关键词替换是否成功替换av和白粉and法轮功习x近x平能不能替换呢？你妈的";
        System.out.println("替换后的s:" + SensitiveWordFilter.getInstance().filter(data));
    }
}
