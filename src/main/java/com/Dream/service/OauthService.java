package com.Dream.service;

import com.Dream.entity.Oauth;

import javax.servlet.http.HttpSession;

public interface OauthService {
    String getAccessToken(String code);

    String getOpenID(String accessToken);

    boolean login(String openID, HttpSession session);

    boolean bind(String openID, String account);

    Oauth insert(Oauth oauth);

    Oauth update(Oauth oauth);

    Oauth findByOpenID(String openID);

    Oauth findByUserID(String userID);
}
