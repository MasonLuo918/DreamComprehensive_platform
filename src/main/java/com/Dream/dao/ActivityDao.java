package com.Dream.dao;

import com.Dream.entity.Activity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityDao extends BaseDao<Activity> {
    List<Activity> selectByName(String name);
    
    /**
     *  返回一个时间段里面的活动，当startDate为null，则查询endDate之前的活动
     *  当endDate为null，则查询startDate之后的活动
     *  两个都为null，则查询所有活动
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return
     */
    List<Activity> selectByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}