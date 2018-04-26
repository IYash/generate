package com.person.spi;

/**
 * Created by huangchangling on 2018/4/24.
 */
public class MysqlQuery implements DatabaseInterface {
    @Override
    public void querySth() {
        System.out.println("mysql database query");
    }
}
