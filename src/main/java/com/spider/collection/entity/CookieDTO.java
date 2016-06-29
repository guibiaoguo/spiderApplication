package com.spider.collection.entity;

/**
 * Created by Administrator on 2016/1/20.
 */

public class CookieDTO {

    private Long id;

    private String userName;

    private String password;

    private String cookie;

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
