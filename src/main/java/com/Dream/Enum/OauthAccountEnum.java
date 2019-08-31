package com.Dream.Enum;

/**
 * @author masonluo
 * 用来保存应用账户信息的枚举类
 */
public enum  OauthAccountEnum {

    YIBAN("7670ac12371bee65","d8a36533e2a15baa0d7a1211b8053658","http://localhost:8080/oauth/yiban/redirect");

    private String appID;

    private String appSecret;

    private String redirectURL;

     OauthAccountEnum(String appID, String appSecret, String redirectURL) {
        this.appID = appID;
        this.appSecret = appSecret;
        this.redirectURL = redirectURL;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }
}
