package com.person.mybatis.shardserver.config;


import com.person.mybatis.shardserver.entity.ShardMapper;
import com.person.mybatis.shardserver.entity.ShardRule;
import com.person.mybatis.shardserver.entity.SqlInfo;
import com.person.mybatis.shardserver.entity.Table;
import com.person.mybatis.shardserver.util.Delimiters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {
    private static final Logger logger= LoggerFactory.getLogger(Configuration.class);

    private ShardRule shardRule;

    private Map<String,SqlInfo> sqlMap;

    public void init(){
        long start = System.currentTimeMillis();
        logger.info("begin to load start config");
        try{
            this.sqlMap=new HashMap<>();
            this.shardRule = XMLConfigReader.loadShardConfig();
            List<ShardMapper> mappers = XMLConfigReader.loadMapperConfig();
            for(ShardMapper mapper:mappers){
                for(SqlInfo sqlInfo:mapper.getSqlInfos()){
                    String sqlId = mapper.getNamespace()+Delimiters.CH_DOT+sqlInfo.getId();
                    sqlMap.put(sqlId,sqlInfo);
                }
            }
        }catch (Exception e){
            logger.error("load shard config failed,cause:{}",e);
        }
        long end=System.currentTimeMillis();
        logger.info("end to load start config. cost time:{}",end-start);
    }
    public ShardRule getShardRule() { return shardRule;}
    public void setShardRule(ShardRule shardRule) {this.shardRule=shardRule;}
    public boolean contiansSqlId(String sqlId){return sqlMap.containsKey(sqlId);}
    public SqlInfo getSqlInfo(String sqlId){return sqlMap.get(sqlId);}
    public Table getTable(String tableName){
        return shardRule.getTable(tableName);
    }
}
