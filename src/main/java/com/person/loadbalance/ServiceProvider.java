package com.person.loadbalance;

/**
 * Created by huangchangling on 2018/6/25.
 * 服务的提供者，提供简单的权重和名称配置
 */
public class ServiceProvider {
    private String serviceName;
    private int serviceWeight;
    //通过key去区分提供的服务，原则上key应该是个服务列表
    private String key;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServiceWeight() {
        return serviceWeight;
    }

    public void setServiceWeight(int serviceWeight) {
        this.serviceWeight = serviceWeight;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
