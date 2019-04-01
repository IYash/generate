package com.person.mybatis.shardserver.entity;

public class ParamItem {
    private String name;
    private String columnName;
    private ItemType type;
    private String enumIndex;
    private String replace;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getEnumIndex() {
        return enumIndex;
    }

    public void setEnumIndex(String enumIndex) {
        this.enumIndex = enumIndex;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }
}
