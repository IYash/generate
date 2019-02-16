package com.person.protocol.model;

import java.io.Serializable;
import java.util.Date;

/**
 * book基本信息
 */
public class Book implements Serializable {

    private String author;
    private Date publishTime;
    private String desc;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public BookBuilder bookBuilder(){
        return new BookBuilder();
    }
    public class BookBuilder{

        public BookBuilder buildAuthor(String author){
            setAuthor(author);
            return this;
        }
        public BookBuilder buildPublishDate(Date date){
            setPublishTime(date);
            return this;
        }
        public BookBuilder buildDesc(String desc){
            setDesc(desc);
            return this;
        }
    }
    @Override
    public String toString() {
        return "Book{" +
                "author='" + author + '\'' +
                ", publishTime=" + publishTime +
                ", desc='" + desc + '\'' +
                '}';
    }
}
