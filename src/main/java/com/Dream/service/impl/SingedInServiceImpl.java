package com.Dream.service.impl;

import com.Dream.commons.cache.Cache;
import com.Dream.service.SingedInService;
import org.springframework.beans.factory.annotation.Autowired;

public class SingedInServiceImpl implements SingedInService {

    @Autowired
    private Cache cache;


    @Override
    public String start(Integer activityID, Integer time) {
        return null;
    }
}
