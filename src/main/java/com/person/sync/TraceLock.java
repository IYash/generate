package com.person.sync;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author huangchangling on 2018/2/11 0011
 */
public class TraceLock {
    private int scale;
    Lock lock = new ReentrantLock();
    public  CyclicBarrier cyclicBarrier ;

    public TraceLock(int scale) {
        this.scale = scale;
        cyclicBarrier = new CyclicBarrier(scale);
    }

    public void lockBody() throws Exception {
        //通过sleep 10s模拟资源占用时间
            System.out.println("get lock ======================"+Thread.currentThread().getName());
            Thread.sleep(10000);

    }
    public void acquireLock(){
        lock.lock();//WAITING(parking) at sun.misc.Unsafe.park(Native Method)
        try {
            lockBody();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    class LocalJob implements Runnable{
        @Override
        public void run() {
            System.out.println("begin to work======================"+Thread.currentThread().getName());
            acquireLock();//内部类可以直接访问
        }
    }
    public static void main(String[] args) {
        int scale  = 5;
        final TraceLock lock = new TraceLock(scale);
        for(int i =0 ;i<scale;i++){

             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     try {
                         lock.cyclicBarrier.await();
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                     lock.new LocalJob().run();
                 }
             }).start();
        }
    }
}
