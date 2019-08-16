package com.Dream.commons.cache;

import java.util.concurrent.Future;

/**
 * 内存缓存实体
 * @param <T>
 */
public class Entity<T>{
    //键值对的value
    private T value;
    //定时器Future
    private Future future;
    public Entity(T value, Future future) {
        this.value = value;
        this.future = future;
    }
    /**
     * 获取值
     *
     * @return
     */
    public T getValue() {
        return value;
    }
    /**
     * 获取Future对象
     *
     * @return
     */
    public Future getFuture() {
        return future;
    }
}
