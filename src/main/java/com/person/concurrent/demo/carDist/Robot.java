package com.person.concurrent.demo.carDist;

import java.util.concurrent.BrokenBarrierException;

/**
 * @author huangchangling on 2018/2/12 0012
 */
abstract class Robot implements Runnable {

    private RobotPool pool;
    public Robot(RobotPool p){pool=p;}
    protected Assembler assembler;
    public Robot assignAssembler(Assembler assembler){
        this.assembler = assembler;
        return this;
    }
    private boolean engage = false;
    public synchronized void engage(){
        engage = true;
        notifyAll();
    }
    //the part of run() that's different for each robot
    abstract protected void performService();
    public void run(){
        try {
        powerDown();//wait until needed
        while(!Thread.interrupted()) {
            performService();
            assembler.barrier().await();//Synchronize
            //we're done with that job..
            powerDown();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

        System.out.println(this + " off");
        }

    private synchronized void powerDown() throws InterruptedException {
        engage = false;
        assembler = null;//Disconnect from the Assembler
        //put ourselves  back in the available pool
        pool.release(this);
        while(!engage) //power down
            wait();
    }
    public String toString(){
        return getClass().getName();
    }
}
