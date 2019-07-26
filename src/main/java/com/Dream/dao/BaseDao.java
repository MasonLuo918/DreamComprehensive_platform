package com.Dream.dao;

import java.util.List;

public interface BaseDao<T> {
    int insert(T t);

    int update(T t);

    int delete(Integer id);

    /**
     * 新建一个T对象，然后传进来，根据这个对象
     * 里面的属性进行查找，使用动态sql
     * @param t
     * @return
     */
    T selectOne(T t);
}
