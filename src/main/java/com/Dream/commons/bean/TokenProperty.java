package com.Dream.commons.bean;

import java.io.Serializable;

public class TokenProperty implements Serializable {

    // 活动id
    private Integer id;

    // 活动名称
    private String name;

    // 生成的token
    private String token;

    // 前端中间处理url
    private String url;

    // 有效期,单位为秒
    private Integer validity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getValidity() {
        return validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }
}
