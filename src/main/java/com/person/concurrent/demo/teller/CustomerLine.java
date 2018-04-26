package com.person.concurrent.demo.teller;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author huangchangling on 2018/1/15 0015
 */
public class CustomerLine extends ArrayBlockingQueue<Customer> {
    public CustomerLine(int capacity) {
        super(capacity);
    }
    public String toString(){
        if (this.size() == 0)
            return "[Empty]";
        StringBuilder result = new StringBuilder();
        for (Customer customer:this)
            result.append(customer);
        return result.toString();
        }
    }

