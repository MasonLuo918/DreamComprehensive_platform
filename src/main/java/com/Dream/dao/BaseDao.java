package com.Dream.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

public interface BaseDao<T> {

    /**
     * 插入一个对象
     * @param t
     * @return
     */
    int insert(T t);

    /**
     * 根据id更新一个对象
     * @param t
     * @return
     */
    int update(T t);

    /**
     * 根据id删除一个对象
     * @param id
     * @return
     */
    int delete(Integer id);

    /**
     * 新建一个T对象，然后传进来，根据这个对象
     * 里面的属性进行查找，使用动态sql
     * @param t
     * @return
     */
    T selectOne(T t);
}
