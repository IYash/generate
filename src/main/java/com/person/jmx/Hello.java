package com.person.jmx;

/**
 * Created by huangchangling on 2018/10/16.
 */
public class Hello implements HelloMBean {
    private String name;
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String printHello() {
        return "Hello " + name;
    }

    @Override
    public String printHello(String whoName) {
        return "Hello " + whoName;
    }
}
