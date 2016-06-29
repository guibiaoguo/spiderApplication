package com.spider.collection.entity;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Created by Administrator on 2015/11/26.
 */
public class HelpTaskDTO{

    private Long id;

    private String httpMethod;

    private String helpUrl;

    private Map parame = new HashedMap();

    private String rightCode;

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHelpUrl() {
        return helpUrl;
    }

    public void setHelpUrl(String helpUrl) {
        this.helpUrl = helpUrl;
    }

    public Map getParame() {
        return parame;
    }

    public void setParame(Map parame) {
        this.parame = parame;
    }

    public String getRightCode() {
        return rightCode;
    }

    public void setRightCode(String rightCode) {
        this.rightCode = rightCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
