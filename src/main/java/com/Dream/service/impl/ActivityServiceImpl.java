package com.Dream.service.impl;

import com.Dream.dao.ActivityDao;
import com.Dream.entity.Activity;
import com.Dream.service.ActivityService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityDao activityDao;

    @Override
    public int insert(Activity activity) {
        return activityDao.insert(activity);
    }

    @Override
    public int update(Activity activity) {
        return activityDao.update(activity);
    }

    @Override
    public List<Activity> findByRegisterID(Integer departmentID, Integer sectionID, int page, int size) {
        if(size == 0){
            size = ActivityService.SIZE;
        }
        int start = (page - 1)* size;
        RowBounds rowBounds = new RowBounds(start, size);
        return activityDao.selectByRegisterIDForRowBounds(departmentID, sectionID,rowBounds);
    }

    @Override
    public List<Activity> findByRegisterID(Integer departmentID, Integer sectionID, int page) {
        return findByRegisterID(departmentID, sectionID, page, ActivityService.SIZE);
    }

    @Override
    public List<Activity> findByRegisterID(Integer departmentID, Integer sectionID) {
        return activityDao.selectByRegisterID(departmentID, sectionID);
    }

    @Override
    public int activityCount(Integer departmentID, Integer sectionID) {
        return activityDao.count(departmentID, sectionID);
    }

    @Override
    public int activityCount(Integer departmentID) {
        return activityCount(departmentID, null);
    }

}
