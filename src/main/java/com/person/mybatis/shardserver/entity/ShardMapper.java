package com.person.mybatis.shardserver.entity;

import java.util.ArrayList;
import java.util.List;

public class ShardMapper {
    private String namespace;
    private List<SqlInfo> sqlInfos = new ArrayList<>();

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public List<SqlInfo> getSqlInfos() {
        return sqlInfos;
    }

    public void setSqlInfos(List<SqlInfo> sqlInfos) {
        this.sqlInfos = sqlInfos;
    }
    public void addSqlInfo(SqlInfo sqlInfo){sqlInfos.add(sqlInfo);}
}
