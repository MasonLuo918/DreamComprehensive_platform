package com.Dream.service;

import com.Dream.entity.Activity;
import com.Dream.entity.ActivityProve;

import java.util.List;

public interface ActivityProveService {
    int insert(ActivityProve activityProve);

    int update(ActivityProve activityProve);

    ActivityProve select(ActivityProve activityProve);

    List<ActivityProve> selectByStuNum(String stuNum);

    List<ActivityProve> selectByActivityID(Integer activityID);
}
