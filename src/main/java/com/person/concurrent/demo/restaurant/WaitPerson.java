package com.person.concurrent.demo.restaurant;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author huangchangling on 2018/1/24 0024
 */
public class WaitPerson implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private final Restaurant restaurant;
    BlockingQueue<Plate> filledOrders = new LinkedBlockingQueue<>();
    public WaitPerson(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {


            try {
                while(!Thread.interrupted()){
                    //Blocks until a course is ready
                    Plate plate = filledOrders.take();
                    System.out.println(this + "received "+plate +" delivering to "+plate.getOrder().getCustomer());
                    //将plate交给customer
                    plate.getOrder().getCustomer().deliver(plate);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }

    }
    public String toString(){
        return "WaitPerson "+ id + " ";
    }
    public void placeOrder(Customer customer, Food food) {
        //shouldn't actually block because this is a LinkedBlockingQueue with no size limit
        try {
            restaurant.orders.put(new Order(customer,this,food));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
