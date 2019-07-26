package com.Dream.dao;

import com.Dream.entity.ActivityProve;
import com.sun.tools.rngom.parse.host.Base;

import java.util.List;

public interface ActivityProveDao extends BaseDao<ActivityProve> {
    List<ActivityProve> selectByActivityId(Integer activityId);

    List<ActivityProve> selectByStuName(String stuName);

    int deleteByActivityId(Integer activityId);
}
