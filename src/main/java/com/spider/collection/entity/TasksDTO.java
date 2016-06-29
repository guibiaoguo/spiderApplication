package com.spider.collection.entity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/27.
 */
public class TasksDTO {

    private Long id;

    private String sourceTable;

    private String saveTable;

    private int page;

    private int pageSize;

    private int contentType;

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private List<HelpTaskDTO> HelpTaskDTOs = new ArrayList<>();

    private List<TargetTaskDTO> TargetTaskDTOs = new ArrayList<>();

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getSaveTable() {
        return saveTable;
    }

    public void setSaveTable(String saveTable) {
        this.saveTable = saveTable;
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

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public List<HelpTaskDTO> getHelpTaskDTOs() {
        return HelpTaskDTOs;
    }

    public void setHelpTaskDTOs(List<HelpTaskDTO> HelpTaskDTOs) {
        this.HelpTaskDTOs = HelpTaskDTOs;
    }

    public List<TargetTaskDTO> getTargetTaskDTOs() {
        return TargetTaskDTOs;
    }

    public void setTargetTaskDTOs(List<TargetTaskDTO> TargetTaskDTOs) {
        this.TargetTaskDTOs = TargetTaskDTOs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
