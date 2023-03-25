package com.help.each.core.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Yuanan
 * @date 2023/3/25
 * @description redis工具类
 */
//消除警告
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@RequiredArgsConstructor
@Component
public class RedisUtil {
    private final RedisTemplate rt;

    /**
     * 向redis存入值
     *
     * @param key   键
     * @param value 值
     */
    public <T> void setObject(final String key, final T value) {
        rt.opsForValue().set(key, value);
    }

    /**
     * 向redis存入值
     *
     * @param key      键
     * @param value    值
     * @param timeout  超时
     * @param timeUnit 时间单位
     */
    public <T> void setObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        rt.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置过期时间
     *
     * @param key     键
     * @param timeout 超时(秒)
     * @return 是否成功
     */
    public boolean expire(final String key, final long timeout) {
        return Boolean.TRUE.equals(rt.expire(key, timeout, TimeUnit.SECONDS));
    }

    /**
     * 设置过期时间
     *
     * @param key     键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return 是否成功
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return Boolean.TRUE.equals(rt.expire(key, timeout, unit));
    }

    /**
     * 获得值
     *
     * @param key 键
     * @return 值
     */
    public <T> T getObject(final String key) {
        ValueOperations<String, T> operation = rt.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除值
     *
     * @param key 键
     * @return 是否成功
     */
    public boolean deleteObject(final String key) {
        return Boolean.TRUE.equals(rt.delete(key));
    }

    /**
     * 删除值
     *
     * @param collection 集合
     * @return long
     */
    public long deleteObject(final Collection collection) {
        return rt.delete(collection);
    }

    /**
     * 存入集合
     *
     * @param key  键
     * @param list 集合
     * @param <T>  泛型
     * @return count
     */
    public <T> long setList(final String key, final List<T> list) {
        Long count = rt.opsForList().rightPushAll(key, list);
        return count == null ? 0 : count;
    }

    /**
     * 获得集合
     *
     * @param key 键
     * @param <T> 泛型
     * @return 集合
     */
    public <T> List<T> getList(final String key) {
        return rt.opsForList().range(key, 0, -1);
    }

    /**
     * @param key 键
     * @param set 集合
     * @param <T> 泛型
     * @return 不知道
     */
    public <T> BoundSetOperations<String, T> setSet(final String key, final Set<T> set) {
        BoundSetOperations<String, T> setOperation = rt.boundSetOps(key);
        for (T t : set) {
            setOperation.add(t);
        }
        return setOperation;
    }

    /**
     * 获得set集合
     *
     * @param key 键
     * @param <T> 泛型
     * @return set集合
     */
    public <T> Set<T> getSet(final String key) {
        return rt.opsForSet().members(key);
    }

    /**
     * 设置map集合
     *
     * @param key 键
     * @param map map
     */
    public <T> void setMap(final String key, final Map<String, T> map) {
        if (map != null) {
            rt.opsForHash().putAll(key, map);
        }
    }

    /**
     * 获得集合
     *
     * @param key 键
     * @return map
     */
    public <T> Map<String, T> getMap(final String key) {
        return rt.opsForHash().entries(key);
    }

    /**
     * @param key   键
     * @param hKey  不知道
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        rt.opsForHash().put(key, hKey, value);
    }

    /**
     * @param key  键
     * @param hKey
     * @param <T>
     * @return
     */
    public <T> T getMapValue(final String key, final String hKey) {
        HashOperations<String, String, T> opsForHash = rt.opsForHash();
        return opsForHash.get(key, hKey);
    }

    public void delMapValue(final String key, final String hkey) {
        HashOperations hashOperations = rt.opsForHash();
        hashOperations.delete(key, hkey);
    }


    public <T> List<T> getMultiMapValue(final String key, final Collection<Object> hKeys) {
        return rt.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 获得所有的见
     *
     * @param pattern 匹配字符串
     */
    public Collection<String> keys(final String pattern) {
        return rt.keys(pattern);
    }

}
