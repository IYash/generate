package com.person.mybatis.shardserver.entity;

import java.util.HashMap;
import java.util.Map;

public class ShardRule {
    private Map<String,Table> tableMap = new HashMap<>();
    public void putTable(Table table){
        tableMap.put(table.getName(),table);
    }
    public Table getTable(String tableName){
        return tableMap.get(tableName);
    }
}
