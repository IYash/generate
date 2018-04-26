package com.person.concurrent.demo.restaurant;

import java.util.concurrent.SynchronousQueue;

/**
 * @author huangchangling on 2018/1/24 0024
 */
public class Customer implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private final WaitPerson waitPerson;
    //only one course at a time can be received
    private SynchronousQueue<Plate> placeSetting = new SynchronousQueue<>();

    public Customer(WaitPerson waitPerson) {
        this.waitPerson = waitPerson;
    }
    public void deliver(Plate p) throws InterruptedException {
        //only blocks if customer is still eating the previous course
        placeSetting.put(p);
    }
    @Override
    public void run() {
        for(Course course:Course.values()) {
            Food food = course.randomSelection();
            try {
                //模拟顾客下单，waitPerson将订单提交给restaurant
                waitPerson.placeOrder(this, food);
                //Blocking until course has been delivered
                System.out.println(this + " eating " + placeSetting.take());
            } catch (InterruptedException e) {
                System.out.println(this + " waiting for " + course + " interrupted");
                break;
            }
        }
        System.out.println(this + " finished meal,leaving");
        }
    public String toString(){
        return "Customer "+id +" ";
    }

}
