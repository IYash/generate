package com.person.concurrent.demo.carDist;

/**
 * @author huangchangling on 2018/2/12 0012
 */
public class Reporter implements Runnable {

    private CarQueue carQueue;
    public Reporter(CarQueue cq){carQueue = cq;}
    @Override
    public void run() {
            try {
                while(!Thread.interrupted())
                System.out.println(carQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        System.out.println("Reporter off");
    }
}
