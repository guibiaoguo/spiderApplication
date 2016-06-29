package com.spider.collection.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Administrator on 2015/12/2.
 */
public class CollectionData {

    private Map<String,BlockingDeque<Map>> datas = new HashMap<>();
    private Map<String,BlockingDeque<Map>> links = new HashMap<>();
    private List<Map> requests = new ArrayList<>();

    private String sourceLink;

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public List<Map> getRequests() {
        return requests;
    }

    public void setRequests(List<Map> requests) {
        this.requests = requests;
    }

    public Map<String, BlockingDeque<Map>> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, BlockingDeque<Map>> datas) {
        this.datas = datas;
    }

    public Map<String, BlockingDeque<Map>> getLinks() {
        return links;
    }

    public void setLinks(Map<String, BlockingDeque<Map>> links) {
        this.links = links;
    }

}
