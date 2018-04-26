package com.person.concurrent.demo.teller;

/**
 *
 * @author huangchangling on 2018/1/15 0015
 */
public class Customer {
    private final int serviceTime;
    public Customer(int serviceTime) {
        this.serviceTime = serviceTime;
    }
    public int getServiceTime(){return serviceTime;}
    public String toString(){
        return "["+serviceTime+"]";
    }
}
