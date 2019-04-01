package com.person.mybatis.shardserver.entity;

import java.util.HashMap;
import java.util.Map;

public enum ItemType {

    INTEGER("int"),
    STRING("string"),
    DATE("date"),ENUM("enum"),
    ;
    private final String TYPE_CODE;
    private ItemType(String code){
        this.TYPE_CODE=code;
    }
    private static Map<String,ItemType> codeLookup = new HashMap<>();
    static{
        for(ItemType type: ItemType.values()){
            codeLookup.put(type.TYPE_CODE,type);
        }
    }
    public static ItemType forCode(String code){
        return codeLookup.get(code);
    }
}
