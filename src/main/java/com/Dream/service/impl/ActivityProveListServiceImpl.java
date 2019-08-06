package com.Dream.service.impl;

import com.Dream.entity.ActivityProve;
import com.Dream.service.ActivityProveListService;
import com.Dream.service.ActivityProveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActivityProveListServiceImpl implements ActivityProveListService {

    @Autowired
    private ActivityProveService activityProveService;

    @Transactional
    @Override
    public int insertList(List<ActivityProve> list) {
        int count = 0;
        for(ActivityProve activityProve:list){
            count += activityProveService.insert(activityProve);
        }
        return count;
    }
}
