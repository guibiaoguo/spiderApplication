package com.spider.collection.entity;

import org.apache.commons.collections.map.HashedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/27.
 */
public class JobDTO {

    private long id;

    private String dstUrl;

    private String name;
    private int status;
    private Boolean isAuthentication;
    private String authGroup;

    private int contentType;

    private List<ProxyDTO> proxyDTOs = new ArrayList<>();

    private List<CookieDTO> cookieDTOs = new ArrayList<>();

    private Captcha captcha;

    private Map header = new HashedMap();

    private TasksDTO tasksDTO;

    private Map login = new HashedMap();

    private Token token;

    private String domain;
    private String charset = "UTF-8";
    private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0";

    private String loginUrl;

    private String scheduler;

    private int sleepTime = 5000;

    private int retryTimes = 0;

    private int cycleRetryTimes = 0;

    private boolean sleepRandom;

    private int thread = 2;

    private int timeOut = 5000;

    private boolean clearClient;

    private RedisConfig redisConfig;

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    public void setRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    public boolean isClearClient() {
        return clearClient;
    }

    public void setClearClient(boolean clearClient) {
        this.clearClient = clearClient;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
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

    public Boolean getIsAuthentication() {
        return isAuthentication;
    }

    public void setIsAuthentication(Boolean isAuthentication) {
        this.isAuthentication = isAuthentication;
    }

    public String getAuthGroup() {
        return authGroup;
    }

    public void setAuthGroup(String authGroup) {
        this.authGroup = authGroup;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public List<ProxyDTO> getProxyDTOs() {
        return proxyDTOs;
    }

    public void setProxyDTOs(List<ProxyDTO> proxyDTOs) {
        this.proxyDTOs = proxyDTOs;
    }

    public Captcha getCaptcha() {
        return captcha;
    }

    public void setCaptcha(Captcha captcha) {
        this.captcha = captcha;
    }

    public Map getHeader() {
        return header;
    }

    public void setHeader(Map header) {
        this.header = header;
    }

    public TasksDTO getTasksDTO() {
        return tasksDTO;
    }

    public void setTasksDTO(TasksDTO tasksDTO) {
        this.tasksDTO = tasksDTO;
    }

    public Map getLogin() {
        return login;
    }

    public void setLogin(Map login) {
        this.login = login;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
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

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
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

    public boolean isSleepRandom() {
        return sleepRandom;
    }

    public void setSleepRandom(boolean sleepRandom) {
        this.sleepRandom = sleepRandom;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<CookieDTO> getCookieDTOs() {
        return cookieDTOs;
    }

    public void setCookieDTOs(List<CookieDTO> cookieDTOs) {
        this.cookieDTOs = cookieDTOs;
    }
}
