package com.person.concurrent.demo.teller;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author huangchangling on 2018/1/16 0016
 */
public class CustomerGenerator implements Runnable {
    private CustomerLine customers;
    private static Random rand = new Random(47);
    public CustomerGenerator(CustomerLine cq){
        customers = cq;
    }
    @Override
    public void run() {
        while(!Thread.interrupted()){
            try {
                TimeUnit.MICROSECONDS.sleep(rand.nextInt(300));
                customers.put(new Customer(rand.nextInt(1000)));
            } catch (InterruptedException e) {
                System.out.println("CustomerGenerator interruted");
            }
            System.out.println("CustomerGenerator terminating");
        }
    }
}
