package com.spider.collection.entity;

/**
 * Created by Administrator on 2015/10/30.
 */
public class Captcha {

    private Long id;

    private String needCode;
    private String checkCode;
    private String errorCode;
    private String code;
    private boolean flag;
    private String codeUrl;
    private String rightCode;
    private String capUrl;

    public String getCapUrl() {
        return capUrl;
    }

    public void setCapUrl(String capUrl) {
        this.capUrl = capUrl;
    }

    public String getRightCode() {
        return rightCode;
    }

    public void setRightCode(String rightCode) {
        this.rightCode = rightCode;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getNeedCode() {
        return needCode;
    }

    public void setNeedCode(String needCode) {
        this.needCode = needCode;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
