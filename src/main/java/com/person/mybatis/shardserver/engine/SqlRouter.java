package com.person.mybatis.shardserver.engine;

import com.person.mybatis.shardserver.entity.ReplaceItem;
import com.person.mybatis.shardserver.util.ReflectionUtil;
import org.apache.ibatis.mapping.BoundSql;

import java.util.List;

public class SqlRouter {
    public void replaceBoundSql(List<ReplaceItem> replaceItems, BoundSql boundSql){
        for(ReplaceItem item : replaceItems){
            StringBuilder sb = new StringBuilder();
            sb.append("(?i)").append(item.getSrcName());
            String newSql = boundSql.getSql().replaceAll(sb.toString(),item.getTargetName());
            ReflectionUtil.setTarget(boundSql,newSql,"sql");
        }
    }
}
