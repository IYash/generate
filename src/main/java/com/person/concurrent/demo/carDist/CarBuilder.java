package com.person.concurrent.demo.carDist;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author huangchangling on 2018/2/12 0012
 */
public class CarBuilder {

    public static void main(String[] args) throws InterruptedException {
        CarQueue chassisQueue = new CarQueue(),finishingQueue = new CarQueue();
        ExecutorService exec = Executors.newCachedThreadPool();
        RobotPool robotPool = new RobotPool();
        exec.execute(new EngineRobot(robotPool));
        exec.execute(new DriveTrainRobot(robotPool));
        exec.execute(new WheelRobot(robotPool));
        exec.execute(new Assembler(chassisQueue,finishingQueue,robotPool));
        exec.execute(new Reporter(finishingQueue));
        //start everything running by producing chassis
        exec.execute(new ChassBuilder(chassisQueue));
        TimeUnit.SECONDS.sleep(7);
        exec.shutdown();
    }
}
