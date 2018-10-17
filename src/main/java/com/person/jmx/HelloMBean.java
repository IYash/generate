package com.person.jmx;

/**
 * Created by huangchangling on 2018/10/16.
 * 为了实现Standard MBean，必须遵循一套继承规范。
 * 必须为每一个MBean定义一个接口，而且这个接口的名字
 * 必须是其被管理的资源的对象类的名称后面加上MBean，之后
 * 把他们注册到MBeanServer中就可以了
 */
public interface HelloMBean {
    String getName();
    void setName(String name);
    String printHello();
    String printHello(String whoName);
}
