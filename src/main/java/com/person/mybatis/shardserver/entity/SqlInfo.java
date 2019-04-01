package com.person.mybatis.shardserver.entity;

import java.util.ArrayList;
import java.util.List;

public class SqlInfo {
    private String id;
    private ParamInfo param;
    private List<String> refTables = new ArrayList<>();
    private boolean cache = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ParamInfo getParam() {
        return param;
    }

    public void setParam(ParamInfo param) {
        this.param = param;
    }

    public List<String> getRefTables() {
        return refTables;
    }

    public void setRefTables(List<String> refTables) {
        this.refTables = refTables;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }
    public void addRefTable(String table){
        refTables.add(table);
    }
}
