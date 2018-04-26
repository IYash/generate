package com.person.concurrent.demo.restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author huangchangling on 2018/1/24 0024
 */
public class Restaurant implements Runnable{
    private List<WaitPerson> waitPersonList = new ArrayList<>();
    private List<Chef> chefs = new ArrayList<>();
    private ExecutorService exec;
    private static Random rand = new Random(47);
    public BlockingQueue<Order> orders = new LinkedBlockingDeque();
    public Restaurant(ExecutorService e,int nWaitPersons,int nChefs){
        exec = e;
        for(int i=0;i< nWaitPersons;i++){
            WaitPerson waitPerson = new WaitPerson(this);
            waitPersonList.add(waitPerson);
            exec.execute(waitPerson);
        }
        for(int i=0;i<nChefs;i++){
            Chef chef = new Chef(this);
            chefs.add(chef);
            exec.execute(chef);
        }
    }

    @Override
    public void run() {
        try {
        while(!Thread.interrupted()){
            //A new customer arrives;assign a WaitPerson;
            WaitPerson wp = waitPersonList.get(rand.nextInt(waitPersonList.size()));
            Customer c = new Customer(wp);
            exec.execute(c);
            TimeUnit.MILLISECONDS.sleep(100); }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        System.out.println("Restaurant closing");
    }
}
