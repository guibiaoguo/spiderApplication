package com.spider.collection.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/27.
 */
public class MapBuilder {
    private Map<Object,Object> _map;

    public MapBuilder() {
        _map=new HashMap<Object, Object>();
    }

    public static MapBuilder instance() {
        return new MapBuilder();
    }
    public MapBuilder put(Object key,Object value){
        _map.put(key, value);
        return this;
    }
    public Map map(){
        return _map;
    }
}
