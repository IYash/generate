package com.person.mybatis.shardserver.entity;

public class ReplaceItem {
    private String srcName;
    private String targetName;

    public ReplaceItem(String srcName, String targetName) {
        this.srcName = srcName;
        this.targetName = targetName;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
}
