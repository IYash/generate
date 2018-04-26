package com.person.concurrent.demo.carDist;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author huangchangling on 2018/2/3 0003
 */
public class Assembler implements Runnable {

    private CarQueue chassisQueue,finishingQueue;
    private Car car;
    private CyclicBarrier barrier = new CyclicBarrier(4);
    private RobotPool robotPool;
    public Assembler(CarQueue cq,CarQueue fq,RobotPool rp){
        chassisQueue = cq;
        finishingQueue = fq;
        robotPool = rp;
    }
    public Car car(){return car;}
    public CyclicBarrier barrier(){return barrier;}
    @Override
    public void run() {

            //Blocks until chassis is available
            try {
                while(!Thread.interrupted()){
                car = chassisQueue.take();
                //hire robots to perform work
                robotPool.hire(EngineRobot.class,this);
                robotPool.hire(DriveTrainRobot.class,this);
                robotPool.hire(WheelRobot.class,this);
                barrier.await();//until the robots finish
                //put car into finishingQueue for further work
                    finishingQueue.put(car);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        System.out.println("Assembler off");
    }
}
