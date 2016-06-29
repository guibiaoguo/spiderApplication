package com.spider.collection.entity;

/**
 * Created by Administrator on 2015/11/6.
 */
public class TargetTask {

    private String targetUrl;
    private String method;
    private String express;
    private String key;
    private Boolean flag;
    private Boolean isMost;
    private int isList;
    private int page;
    private int pageSize;
    private int isCustom;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Boolean getIsMost() {
        return isMost;
    }

    public void setIsMost(Boolean isMost) {
        this.isMost = isMost;
    }

    public int getIsList() {
        return isList;
    }

    public void setIsList(int isList) {
        this.isList = isList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(int isCustom) {
        this.isCustom = isCustom;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }
}
