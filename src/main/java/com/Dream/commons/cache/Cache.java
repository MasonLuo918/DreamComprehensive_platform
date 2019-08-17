package com.Dream.commons.cache;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 简单的内存缓存工具类
 */
public class Cache {
    //键值对集合
    private final static Map<String, Entity> map = new HashMap<>();
    //定时器线程池，用于清除过期缓存
    private final static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    /**
     * 添加缓存
     *
     * @param key  键
     * @param data 值
     */
    public synchronized static void put(String key, Entity data) {
        Cache.put(key, data, 0);
    }

    /**
     * 添加缓存
     *
     * @param key    键
     * @param data   值
     * @param expire 过期时间，单位：毫秒， 0表示无限长
     */
    public synchronized static void put(String key, Entity data, long expire) {
        Runnable event = new Runnable() {
            @Override
            public void run() {
                synchronized (Cache.class) {
                    map.remove(key);
                }
            }
        };
        put(key, data, expire, event);
    }

    public synchronized static void put(String key, Entity data, long expire, Runnable event) {
        //清除原键值对
        Cache.remove(key);
        //设置过期时间
        if (expire > 0) {
            Future future = executor.schedule(event, expire, TimeUnit.SECONDS);
            data.setFuture(future);
            map.put(key, data);
        } else {
            // 不设置过期时间
            map.put(key, data);
        }
    }

    /**
     * 读取缓存
     *
     * @param key 键
     * @return
     */
    public synchronized static Entity get(String key) {
        return map.get(key);
    }

    /**
     * 读取缓存
     *
     * @param key 键
     *            * @param clazz 值类型
     * @return
     */
    public synchronized static <T> T get(String key, Class<T> clazz) {
        return clazz.cast(Cache.get(key));
    }

    /**
     * 清除缓存
     *
     * @param key
     */
    public synchronized static Entity remove(String key) {
        //清除原缓存数据
        Entity entity = map.remove(key);
        if (entity == null) return null;
        //清除原键值对定时器
        Future future = entity.getFuture();
        if (future != null) future.cancel(true);
        return entity;
    }

    /**
     * 查询当前缓存的键值对数量
     *
     * @return
     */
    public synchronized static int size() {
        return map.size();
    }
}
