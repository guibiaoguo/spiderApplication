package com.spider.collection.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/13.
 */
public class Job {

    private long id;

    private String dstUrl;
    private String name;
    private int status;
    private Boolean isAuthentication;
    private String auth_group;
    private Tasks tasksDTO;
    private int contentType;
    private List<Proxy> proxyDTOs = new ArrayList<>();
    private String domain;//:"renren.com",
    private String charset;//:"utf-8",
    private String userAgent;//:"",
    private Map login;
    private String loginUrl;
    private Captcha captcha;
    private Token token;
    private int sleepTime = 5000;
    private Map header;
    private int retryTimes = 0;

    private int cycleRetryTimes = 0;

    private boolean sleepRandom;

    private int thread = 2;

    private boolean clearClient;

    private String scheduler;

    private int timeOut = 5000;

    private Date createdTime;

    private Date updatedTime;

    private String authGroup;

    public String getAuthGroup() {
        return authGroup;
    }

    public void setAuthGroup(String authGroup) {
        this.authGroup = authGroup;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Map getHeader() {
        return header;
    }


    public void setHeader(Map header) {
        this.header = header;
    }

    public boolean isClearClient() {
        return clearClient;
    }

    public void setClearClient(boolean clearClient) {
        this.clearClient = clearClient;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public boolean isSleepRandom() {
        return sleepRandom;
    }

    public void setSleepRandom(boolean sleepRandom) {
        this.sleepRandom = sleepRandom;
    }
    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public Boolean getIsAuthentication() {
        return isAuthentication;
    }

    public void setIsAuthentication(Boolean isAuthentication) {
        this.isAuthentication = isAuthentication;
    }

    public Captcha getCaptcha() {
        return captcha;
    }

    public void setCaptcha(Captcha captcha) {
        this.captcha = captcha;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public Map getLogin() {
        return login;
    }

    public void setLogin(Map login) {
        this.login = login;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getCycleRetryTimes() {
        return cycleRetryTimes;
    }

    public void setCycleRetryTimes(int cycleRetryTimes) {
        this.cycleRetryTimes = cycleRetryTimes;
    }


    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getDstUrl() {
        return dstUrl;
    }

    public void setDstUrl(String dstUrl) {
        this.dstUrl = dstUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAuth_group() {
        return auth_group;
    }

    public void setAuth_group(String auth_group) {
        this.auth_group = auth_group;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Tasks getTasksDTO() {
        return tasksDTO;
    }

    public void setTasksDTO(Tasks tasksDTO) {
        this.tasksDTO = tasksDTO;
    }

    public List<Proxy> getProxyDTOs() {
        return proxyDTOs;
    }

    public void setProxyDTOs(List<Proxy> proxyDTOs) {
        this.proxyDTOs = proxyDTOs;
    }
}
