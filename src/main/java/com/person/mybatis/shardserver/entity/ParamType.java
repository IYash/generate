package com.person.mybatis.shardserver.entity;

public enum ParamType {
    CLAZZ("class"),MAP("map"),CLASSMAP("classmap"),
    ;
    private String TYPE_CODE;
    private ParamType(String code){
        this.TYPE_CODE =code;
    }

    public static ParamType forCode(String code){
        switch (code){
            case "clazz":return CLAZZ;
            case "map":return MAP;
            case "classmap":return CLASSMAP;
            default:return MAP;
        }
    }
}
