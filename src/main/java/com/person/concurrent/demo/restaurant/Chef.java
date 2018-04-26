package com.person.concurrent.demo.restaurant;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author huangchangling on 2018/1/29 0029
 */
public class Chef implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private final Restaurant restaurant;
    private static Random rand = new Random(47);
    public Chef(Restaurant restaurant) {this.restaurant = restaurant;}

    @Override
    public void run() {

            //Blocks until an order appears
            try {
                while(!Thread.interrupted()){
                    //获取订单
                Order order = restaurant.orders.take();
                    Food requestedItem = order.item();
                    //time to prepare order
                    TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
                    Plate plate = new Plate(order,requestedItem);
                    order.getWaitPerson().filledOrders.put(plate);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        System.out.println(this + " off duty");
    }
    public String toString(){
        return "Chef " +id + " ";
    }
}
