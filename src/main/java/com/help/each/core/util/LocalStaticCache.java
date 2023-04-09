package com.help.each.core.util;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yuanan
 * @date 2023/4/9
 * @description 本地静态缓存Map 单例
 */
public class LocalStaticCache {
    private Map<String, Object> localCache = null;

    private LocalStaticCache() {
        localCache = new ConcurrentHashMap<>();
    }

    public static LocalStaticCache getInstance() {
        return Holder.INSTANCE;
    }

    public void setObject(String k, Object v) {
        localCache.put(k, v);
    }

    public Object getObject(String k) {
        return localCache.get(k);
    }

    public void deleteObject(String k) {
        localCache.remove(k);
    }

    private static class Holder {
        private static final LocalStaticCache INSTANCE = new LocalStaticCache();
    }
}
