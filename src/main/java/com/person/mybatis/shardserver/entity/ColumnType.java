package com.person.mybatis.shardserver.entity;

import java.util.HashMap;
import java.util.Map;

public enum ColumnType {

    INT("int"),STRING("string"),DATE("date");
    private final String TYPE_CODE;

    private static Map<String,ColumnType> codeLookup = new HashMap<>();
    ColumnType(String code){
        this.TYPE_CODE =code;
    }
    static {
        for (ColumnType type : ColumnType.values()) {
            codeLookup.put(type.TYPE_CODE, type);
        }
    }
    public static ColumnType forCode(String code){
        return codeLookup.get(code);
    }
}
