package com.Dream.service;

import com.Dream.entity.Oauth;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface OauthService {
    /**
     * 获取Access_Token
     * @param code 令牌
     * @return api返回的数据
     */
    Map getAccessToken(String code);

    /**
     * 获取user_id的api返回的数据
     * @param accessToken
     * @return
     */
    Map getOpenID(String accessToken);

    /**
     * 利用code，获取access_token 和 user_id 进行登录
     * 返回的map要有如下几个字段:
     * status:
     * 1、如果status 为 1，证明成功登录
     * 2、如果status 为 0，证明登录失败，该用户是首次登录，需要进行邮箱绑定
     * 3、如果status 为 2，说明token已经失效
     * message:
     * 附带的返回信息
     * open_id:
     * 如果status 为 1 或者 为 0， 则需要返回这个open_id,
     * 然后controller再利用其进行相应的操作
     * open_type:
     * 如果status 为 1 或者 为0，也需要返回这个，表示相应的open_type（哪一个第三方登录）
     * @param code
     * @param session
     * @return
     */
    Map<String, Object> login(String code, HttpSession session);

    boolean bind(String openID, String account);

    Oauth insert(Oauth oauth);

    Oauth update(Oauth oauth);

    Oauth findByOpenID(String openID);

    Oauth findByUserID(String userID);
}
