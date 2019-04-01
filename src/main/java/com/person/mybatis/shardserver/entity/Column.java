package com.person.mybatis.shardserver.entity;

import java.util.HashMap;
import java.util.Map;

public class Column {
    private String name;
    private ColumnType type;
    private String format;
    private String split="";

    private Map<String,String> aliasMap = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }
    public void putAlias(String value,String alias){
        aliasMap.put(value,alias);
    }
    public String getAlias(String value){
        return aliasMap.get(value);
    }
}
