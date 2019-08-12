package com.Dream.dao;

import com.Dream.entity.ActivityProve;
import com.sun.tools.rngom.parse.host.Base;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityProveDao extends BaseDao<ActivityProve> {
    List<ActivityProve> selectByActivityId(Integer activityId);

    List<ActivityProve> selectByStuName(String stuName);

    List<ActivityProve> selectByStuNum(String stuNum);

    int deleteByActivityId(Integer activityId);

    int deleteByActivityIDAndType(@Param("activityID") Integer activityID, @Param("type") Integer type);
}
