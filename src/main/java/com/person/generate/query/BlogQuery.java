package com.person.generate.query;

import org.apache.commons.lang3.StringUtils;


/**
 * @author huangchangling on 2017/11/29 0029
 */
public class BlogQuery extends BaseQuery {

    private Integer id;

    private String message;

    @Override
    public void constructQuery() {
        if(id != null) {
            putCondition(" id = ? ",id);
        }
        if(StringUtils.isNotEmpty(message)){
            putCondition(" message = ? ",message);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
