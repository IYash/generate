package com.person.generate.query;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author huangchangling on 2017/11/29 0029
 * 封装查询条件的语句和参数
 */
public abstract class BaseQuery {

    private List<String> conditions= new ArrayList<>() ;

    private List<Object> params = new ArrayList<>();

    private boolean initFlag = false;

    private void init(){
        if(!initFlag) {
            constructQuery();
            initFlag = true;
        }
    }
    public String getQuery(){
        init();
        StringBuilder query = new StringBuilder(100);

        if(conditions.size()>0){
            query.append(" where ");
            String cons = StringUtils.join(conditions," and ");
            query.append(cons);
        }

        return query.toString();
    }
    protected abstract void constructQuery();

    protected void putCondition(String condition,Object... param){
        conditions.add(condition);
        params.addAll(Arrays.asList(param));
    }
    public Object[] getParams(){
        init();
        return params.toArray();
    }

}
