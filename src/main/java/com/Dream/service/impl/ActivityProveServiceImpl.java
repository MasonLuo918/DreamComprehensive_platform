package com.Dream.service.impl;

import com.Dream.dao.ActivityProveDao;
import com.Dream.entity.ActivityProve;
import com.Dream.service.ActivityProveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityProveServiceImpl implements ActivityProveService {

    @Autowired
    private ActivityProveDao activityProveDao;


    @Override
    public int insert(ActivityProve activityProve) {
        return activityProveDao.insert(activityProve);
    }

    @Override
    public int update(ActivityProve activityProve) {
        return activityProveDao.update(activityProve);
    }

    @Override
    public ActivityProve select(ActivityProve activityProve) {
        return activityProveDao.selectOne(activityProve);
    }

    @Override
    public List<ActivityProve> selectByStuNum(String stuNum) {
        return activityProveDao.selectByStuNum(stuNum);
    }

    @Override
    public List<ActivityProve> selectByActivityID(Integer activityID) {
        return activityProveDao.selectByActivityId(activityID);
    }

    @Override
    public int clearDocRecord(Integer activityID, int type) {
        return activityProveDao.deleteByActivityIDAndType(activityID, type);
    }

}
