package com.spider.collection.entity;

/**
 * Created by Administrator on 2015/10/30.
 */
public class Token {
    private Long id;
    private boolean flag;
    private String token;
    private String tokenUrl;
    private String checkToken;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getCheckToken() {
        return checkToken;
    }

    public void setCheckToken(String checkToken) {
        this.checkToken = checkToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
