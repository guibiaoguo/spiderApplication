package com.spider.collection.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Bill on 2016/1/10.
 */
public class Link {

    private String sourceLink;
    private List<Map<String,Object>> links = new ArrayList<>();

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public List<Map<String, Object>> getLinks() {
        return links;
    }

    public void setLinks(List<Map<String, Object>> links) {
        this.links = links;
    }
}
