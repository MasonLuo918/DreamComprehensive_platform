package com.Dream.service;

import com.Dream.commons.bean.SignedTokenProperty;
import com.Dream.commons.bean.TokenProperty;
import com.Dream.commons.cache.Entity;
import com.Dream.entity.SignIn;

import java.util.List;
import java.util.Set;

public interface SignedInService {

    /**
     * 开启一个活动签到器
     * @param tokenProperty 一个token的属性,用来保存在Cache中
     * @return 返回一个活动签到器的token，用来获取签到二维码
     */
    String start(TokenProperty tokenProperty);

    /**
     * 获取一个二维码，并封装入Entity中
     * @param token
     * @return
     */
    Entity<SignedTokenProperty> getQRCode(String token);

    /**
     * 判断是否能跳转
     * @param token
     * @return
     */
    Entity<SignedTokenProperty> redirect(String token);

    /**
     * 进行签到
     * @param token 生成活动签到器时产生的token
     * @param record 一个学生的签到记录
     * @return
     */
    SignIn doSignIn(String token, SignIn record);

    /**
     * 根据token获取当前签到的情况
     * @param token
     * @return
     */
    Set<SignIn> getRecords(String token);

    List<SignIn> getSignInList(Integer activityID);
}
