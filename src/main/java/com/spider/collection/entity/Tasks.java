package com.spider.collection.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/10/13.
 */
public class Tasks {
    private String key;
    private String express;
    private String method;
    private String sourceTable;

    public String getSaveTable() {
        return saveTable;
    }

    public void setSaveTable(String saveTable) {
        this.saveTable = saveTable;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    private String saveTable;
    private int flag;
    private int isBatch;
    private int isList;
    private int page;
    private int pageSize;
    private int isCustom;
    private int contentType;

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    private List<HelpTask> helpTasks;
    private List<TargetTask> targetTasks;

    public List<TargetTask> getTargetTasks() {
        return targetTasks;
    }

    public void setTargetTasks(List<TargetTask> targetTasks) {
        this.targetTasks = targetTasks;
    }

    public List<HelpTask> getHelpTasks() {
        return helpTasks;
    }

    public void setHelpTasks(List<HelpTask> helpTasks) {
        this.helpTasks = helpTasks;
    }

    public int getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(int isCustom) {
        this.isCustom = isCustom;
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

    public String getKey() {
        return key;
    }

    public int getIsBatch() {
        return isBatch;
    }

    public void setIsBatch(int isBatch) {
        this.isBatch = isBatch;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
