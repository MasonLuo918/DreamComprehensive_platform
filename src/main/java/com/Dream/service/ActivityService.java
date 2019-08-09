package com.Dream.service;

import com.Dream.entity.Activity;
import com.Dream.entity.UploadFile;

import java.util.List;

public interface ActivityService {

    static final int SIZE = 10;

    int insert(Activity activity);

    int update(Activity activity);

    /**
     * 寻找一个部门的所有活动
     * @param departmentID
     * @param sectionID
     * @param page 第几页
     * @param size 每一页多少条
     * @return
     */
    List<Activity> findByRegisterID(Integer departmentID, Integer sectionID, int page, int size);
    List<Activity> findByRegisterID(Integer departmentID, Integer sectionID, int page);
    List<Activity> findByRegisterID(Integer departmentID, Integer sectionID);

    /**
     * 查找这个部门活动的条数
     * @param departmentID
     * @param sectionID
     * @return
     */
    int activityCount(Integer departmentID, Integer sectionID);
    int activityCount(Integer departmentID);
}
