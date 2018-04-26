package com.person.generate.entity;

import java.util.Date;

/**
 * @author huangchangling on 2017/10/16 0016
 */
public class Blog {

    private int id;

    private String message;

    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
