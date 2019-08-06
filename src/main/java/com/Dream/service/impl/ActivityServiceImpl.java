package com.Dream.service.impl;

import com.Dream.dao.ActivityDao;
import com.Dream.entity.Activity;
import com.Dream.service.ActivityService;
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
    public List<Activity> selectByDepartmentIDOrSectionID(Integer departmentID, Integer sectionID) {
        return activityDao.selectByDepartmentIDOrSectionID(departmentID, sectionID);
    }
}
