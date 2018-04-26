package com.person.concurrent.demo.restaurant;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author huangchangling on 2018/2/1 0001
 */
public class RestaurantWithQueues {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        Restaurant restaurant = new Restaurant(exec,5,2);
        exec.execute(restaurant);
        if(args.length >0)//Optional argument
            TimeUnit.SECONDS.sleep(new Integer(args[0]));
        else{
            System.out.println("PRESS 'ENTER' TO QUIT");
            System.in.read();
        }
        exec.shutdownNow();
    }
}
