package com.Dream.dao;

import com.Dream.entity.Activity;

import java.util.Date;
import java.util.List;

public interface ActivityDao extends BaseDao<Activity> {
    List<Activity> selectByName(String name);

    List<Activity> selectByTime(Date startDate, Date endDate);
}