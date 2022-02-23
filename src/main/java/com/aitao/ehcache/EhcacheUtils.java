package com.aitao.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.io.InputStream;
import java.util.Calendar;

public class EhcacheUtils {
    private static final int ONE = 1;
    private static final String DEFAULT_VALUE = "0001";
    private static final int LENGTH = 4;
    private static CacheManager cacheManager = null;
    private static final String CONFIG_FILE_PATH = "./src/main/resources/ehcache.xml";

    /**
     * 初始化缓存管理器
     */
    private static void init() {
        if (cacheManager == null) {
            InputStream is = EhcacheUtils.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH);
            if (is == null) {
                throw new RuntimeException(String.format("配置文件不存在:%s", CONFIG_FILE_PATH));
            }
            cacheManager = new CacheManager(is);
        }
    }

    /**
     * @return
     */
    public synchronized static String generateCode(String cacheName) {
        init();
        Cache cache = cacheManager.getCache(cacheName);
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        Element element = cache.get(year);
        if (element == null) {
            element = new Element(year, ONE);
            cache.put(element);
            return year + DEFAULT_VALUE;
        }

        int currentValue = Integer.parseInt(String.valueOf(element.getObjectValue()));
        StringBuilder str = new StringBuilder(String.valueOf(currentValue + 1));
        while (str.length() < LENGTH) {
            str.insert(0, "0");
        }
        cache.remove(year);// 清除
        element = new Element(year, currentValue + 1);
        cache.put(element);
        cache.flush();
        shutdown();
        return year + str;
    }

    /**
     * 关闭缓存管理器
     */
    private static void shutdown() {
        if (cacheManager != null) {
            cacheManager.shutdown();
            cacheManager = null;
        }
    }
}