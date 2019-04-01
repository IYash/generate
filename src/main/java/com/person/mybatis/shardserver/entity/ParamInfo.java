package com.person.mybatis.shardserver.entity;

import java.util.HashMap;
import java.util.Map;


public class ParamInfo {
    private String name;
    private ParamType type;
    private Map<String,ParamItem> itemMap = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParamType getType() {
        return type;
    }

    public void setType(ParamType type) {
        this.type = type;
    }

    public Map<String, ParamItem> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<String, ParamItem> itemMap) {
        this.itemMap = itemMap;
    }
    public void putItem(ParamItem item){
        itemMap.put(item.getColumnName(),item);
    }
    public ParamItem getItem(String columnName){
        return itemMap.get(columnName);
    }
}
