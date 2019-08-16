package com.Dream.dao;

import com.Dream.entity.SignIn;

import java.util.List;

public interface SignInDao extends BaseDao<SignIn>{

    /**
     * 根据活动Id查找签到表
     * @param activityId
     * @return
     */
    List<SignIn> selectByActivityId(Integer activityId);

    /**
     * 根据学生学号查找签到表
     * @param stuId
     * @return
     */
    List<SignIn> selectBySignIn(Integer stuId);

    /**
     * 根据活动Id删除签到表
     * @param activity
     * @return
     */
    int deleteByActivityId(Integer activity);

}
