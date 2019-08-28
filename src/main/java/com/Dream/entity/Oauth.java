package com.Dream.entity;

import com.Dream.Enum.OauthTypeEnum;

public class Oauth {
    private Integer id;

    private String userID;

    private String openID;

    private String accessToken;

    private OauthTypeEnum openType;

    // 令牌过期时间，单位为秒
    private Long expiredTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public OauthTypeEnum getOpenType() {
        return openType;
    }

    public void setOpenType(OauthTypeEnum openType) {
        this.openType = openType;
    }

    public Long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }
}
