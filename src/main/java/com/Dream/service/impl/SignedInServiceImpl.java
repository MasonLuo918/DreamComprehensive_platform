package com.Dream.service.impl;

import com.Dream.commons.bean.SignedTokenProperty;
import com.Dream.commons.bean.TokenProperty;
import com.Dream.commons.cache.Cache;
import com.Dream.commons.cache.Entity;
import com.Dream.dao.ActivityDao;
import com.Dream.dao.SignInDao;
import com.Dream.entity.Activity;
import com.Dream.entity.SignIn;
import com.Dream.service.SignedInService;
import com.Dream.util.Base64;
import com.Dream.util.MD5Util;
import com.Dream.util.QRCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SignedInServiceImpl implements SignedInService {

    private final Integer VALIDITY = 10;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private RedisTemplate<String, SignIn> redisTemplate;

    @Autowired
    private SignInDao signInDao;

    @Override
    public String start(TokenProperty tokenProperty) {
        Activity queryActivity = new Activity();
        queryActivity.setId(tokenProperty.getId());
        Activity queryResult = activityDao.selectOne(queryActivity);
        if(queryResult == null){
            return null;
        }
        String token = String.valueOf(tokenProperty.getId()) + String.valueOf(System.currentTimeMillis());
        // 将token数据加密
        token = MD5Util.getMD5(token);
        String finalToken = token;
        // 生成一个token属性类，在里面保存数据
        tokenProperty.setName(queryResult.getName());
        tokenProperty.setToken(token);
        // 生成entity
        Entity<TokenProperty> entity = new Entity<>(tokenProperty);
        // 生成一个过期事件
        Runnable event = new Runnable() {
            @Override
            public void run() {
                // 清除这个token
                synchronized (Cache.class){
                    Entity<TokenProperty> entity = Cache.remove(finalToken);
                    dealWithTokenExpire(entity.getValue().getToken());
                }
            }
        };
        // 设置过期时间，放入缓存类
        Cache.put(token, entity, tokenProperty.getValidity(),event);
        return token;
    }

    @Override
    public Entity<SignedTokenProperty> getQRCode(String token) {
        // 获取活动签到器二维码获取令牌的实体
        Entity<TokenProperty> codeTokenEntity = Cache.get(token);
        if(codeTokenEntity == null){
            return null;
        }
        TokenProperty tokenProperty = codeTokenEntity.getValue();
        // 生成签到token，也就是二维码的有效期令牌
        String signedToken = String.valueOf(tokenProperty.getId()) + System.currentTimeMillis();
        signedToken = MD5Util.getMD5(signedToken);
        SignedTokenProperty property = new SignedTokenProperty();
        // 设置中转url
        property.setUrl(tokenProperty.getUrl());
        property.setId(tokenProperty.getId());
        property.setName(tokenProperty.getName());
        property.setValidity(VALIDITY);
        // 设置令牌，保存(生成活动签到器的令牌)
        property.setToken(token);
        // 参数
        Map<String, String> param = new HashMap<>();
        param.put("token",signedToken);
        System.out.println(signedToken);
        String imgBase64 = Base64.getImageStr(QRCode.createQRCode(property.getUrl(),param));
        property.setImgBase64(imgBase64);
        // 生成放入缓存的实体
        Entity<SignedTokenProperty> entity = new Entity<>(property);
        // 保存入Cache中,并且设置有效期为VALIDITY
        Cache.put(signedToken, entity, entity.getValue().getValidity());
        return entity;
    }

    @Override
    public Entity<SignedTokenProperty> redirect(String token) {
        return Cache.get(token);
    }

    @Override
    public SignIn doSignIn(String token, SignIn record) {
        Entity<TokenProperty> entity = Cache.get(token);
        if(entity == null){
            return null;
        }
        record.setCreateTime(LocalDate.now());
        // 设置签到学生参加的活动id
        record.setActivityID(entity.getValue().getId());
        // 判断是否有这个key，没有则新建,有则加入
        redisTemplate.opsForSet().add(token, record);
        return record;
    }

    @Override
    public Set<SignIn> getRecords(String token) {
        return redisTemplate.opsForSet().members(token);
    }

    @Override
    public List<SignIn> getSignInList(Integer activityID) {
        return signInDao.selectByActivityId(activityID);
    }

    public void dealWithTokenExpire(String token) {
          Set<SignIn> members = redisTemplate.opsForSet().members(token);
        if(members.size() != 0){
            for(SignIn record:members){
                // 将记录都插入表中
                signInDao.insert(record);
            }
        }
        redisTemplate.delete(token);
    }
}
