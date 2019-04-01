package com.person.mybatis.shardserver.engine;

import com.person.mybatis.shardserver.config.Configuration;
import com.person.mybatis.shardserver.entity.*;
import com.person.mybatis.shardserver.util.DateFormatUtil;
import com.person.mybatis.shardserver.util.ReflectionUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SqlResolver {
    private static final Logger logger = LoggerFactory.getLogger(SqlResolver.class);

    private Configuration config;
    private Date shardTableStartTime;

    public boolean isSharded(String sqlId){return config.contiansSqlId(sqlId);}
    public SqlInfo getSqlInfo(String sqlId){
        return config.getSqlInfo(sqlId);
    }
    public Table getTable(String tableName){
        return config.getTable(tableName);
    }

    public List<ReplaceItem> parseSqlInfo(String sqlId, Object paramObject, boolean isRoute){
        boolean replace = true;
        List<ReplaceItem> replaceItems = new ArrayList<>();
        try{
            SqlInfo sqlInfo = getSqlInfo(sqlId);
            switch(sqlInfo.getParam().getType()){
                case MAP:
                    replace = parseMap(sqlInfo,paramObject,replaceItems,isRoute);
                    break;
                case CLAZZ:
                    replace = parseClass(sqlInfo,paramObject,replaceItems,isRoute);
                    break;
                case CLASSMAP:
                    replace = parseClassMap(sqlInfo,paramObject,replaceItems,isRoute);
                    break;
            }
            if(!replace)
                return Collections.EMPTY_LIST;
        }catch (Exception e){
            logger.error("Parse sql info failed,cause by:",e);
        }
        return replaceItems;
    }

    private boolean parseClassMap(SqlInfo sqlInfo, Object paramObject, List<ReplaceItem> replaceItems, boolean isRoute) throws Exception{
        ParamInfo param = sqlInfo.getParam();
        Map map = (Map)paramObject;
        Object clazz = map.get(param.getName());
        return reflectClass(param,sqlInfo,clazz,replaceItems,isRoute);
    }

    private boolean parseClass(SqlInfo sqlInfo, Object paramObject, List<ReplaceItem> replaceItems, boolean isRoute) throws Exception{
        ParamInfo paramInfo = sqlInfo.getParam();
        return reflectClass(paramInfo,sqlInfo,paramObject,replaceItems,isRoute);
    }

    private boolean parseMap(SqlInfo sqlInfo, Object paramObject, List<ReplaceItem> replaceItems, boolean isRoute) throws Exception {
        ParamInfo paramInfo = sqlInfo.getParam();
        Map map = (Map)paramObject;
        Object clazz = map.get(paramInfo.getName());
        return reflectClass(paramInfo,sqlInfo,clazz,replaceItems,isRoute);
    }
    private boolean reflectClass(ParamInfo param, SqlInfo sqlInfo, Object clazz, List<ReplaceItem> replaceItems, boolean isRoute) throws Exception{
        for(String tableName:sqlInfo.getRefTables()){
            Table table = getTable(tableName);
            StringBuilder newTable = new StringBuilder();
            if(isRoute) newTable.append(tableName);
            for(Column column:table.getColumns()){
                ParamItem item = param.getItem(column.getName());
                Object paramVal = getParamValue(item,clazz);
                boolean isAppend = appendAlias(newTable,column,paramVal);
                if(!isAppend) return false;
                if(!isRoute|| StringUtils.isBlank(item.getReplace())) continue;
                replaceItems.add(new ReplaceItem(item.getReplace(),item.getColumnName()));
            }
            replaceItems.add(new ReplaceItem(tableName,newTable.toString()));
        }
        return true;
    }

    private boolean appendAlias(StringBuilder newTable,Column column,Object paramVal){
        newTable.append(column.getSplit());
        if(column.getType()!=ColumnType.DATE){
            newTable.append(column.getAlias(paramVal.toString()));
        }else{
            Date postDate = (Date)paramVal;
            if(shardTableStartTime !=null && postDate.before(shardTableStartTime)) return false;
            newTable.append(DateFormatUtil.format(postDate,column.getFormat()));
        }
        return true;
    }
    private Object getParamValue(ParamItem item,Object paramObj) throws Exception{
        switch(item.getType()){
            case INTEGER:return ReflectionUtil.findTarget(Integer.class,paramObj,item.getName());
            case STRING:return ReflectionUtil.findTarget(String.class,paramObj,item.getName());
            case DATE:return ReflectionUtil.findTarget(Date.class,paramObj,item.getName());
            case ENUM:
                Object enumObj = ReflectionUtil.findTarget(Enum.class,paramObj,item.getName());
                return ReflectionUtil.findTarget(Integer.class,enumObj,item.getEnumIndex());
            default:
                throw new RuntimeException("Illegal item tyep:"+item.getType());
        }
    }
    public void setShardTableStartTime(String shardTableStartTime){
        if(shardTableStartTime == null) return;
        String[] strs = shardTableStartTime.split("-");
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Integer.parseInt(strs[0]),Integer.parseInt(strs[1])-1,Integer.parseInt(strs[2]));
        this.shardTableStartTime = cal.getTime();
    }
    public void setConfig(Configuration config){this.config=config;}
}
