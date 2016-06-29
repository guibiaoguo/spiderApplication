package com.spider.collection.entity;

import java.util.Map;

/**
 * Created by Administrator on 2015/11/6.
 */
public class HelpTask{

    private Long id;

    private String httpMethod;
    private String helpUrl;
    private Map parames;
    private Map variableParameters;
    private String errorCode;
    private String codeExpress;
    private String rightCode;
    private String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

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

    public Map getParames() {
        return parames;
    }

    public void setParames(Map parames) {
        this.parames = parames;
    }

    public Map getVariableParameters() {
        return variableParameters;
    }

    public void setVariableParameters(Map variableParameters) {
        this.variableParameters = variableParameters;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getCodeExpress() {
        return codeExpress;
    }

    public void setCodeExpress(String codeExpress) {
        this.codeExpress = codeExpress;
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
