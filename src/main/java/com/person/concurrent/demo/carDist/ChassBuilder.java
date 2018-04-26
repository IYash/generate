package com.person.concurrent.demo.carDist;

import java.util.concurrent.TimeUnit;

/**
 * @author huangchangling on 2018/2/3 0003
 */
public class ChassBuilder implements Runnable {
    private CarQueue carQueue;
    private int counter = 0;
    public ChassBuilder(CarQueue cq){carQueue = cq;}
    @Override
    public void run() {

            try {
                while(!Thread.interrupted()){
                TimeUnit.MILLISECONDS.sleep(500);
                //make chassis
                Car c = new Car(counter++);
                System.out.println("ChassicBuilder create " + c);
                carQueue.add(c);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        System.out.println("ChassisBuilder off");
    }
}
