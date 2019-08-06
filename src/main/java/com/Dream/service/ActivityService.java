package com.Dream.service;

import com.Dream.entity.Activity;

import java.util.List;

public interface ActivityService {
    int insert(Activity activity);

    int update(Activity activity);

    List<Activity> selectByDepartmentIDOrSectionID(Integer departmentID, Integer sectionID);
}
