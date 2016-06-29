package com.spider.collection.entity;

/**
 * Created by Administrator on 2015/11/27.
 */
public class TargetTaskDTO {

    private Long id;

    private String targetUrl;

    private String method;

    private String express;

    private String key;

    private Boolean flag;

    private Boolean isMost;

    private String saveTable;

    public String getSaveTable() {
        return saveTable;
    }

    public void setSaveTable(String saveTable) {
        this.saveTable = saveTable;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
