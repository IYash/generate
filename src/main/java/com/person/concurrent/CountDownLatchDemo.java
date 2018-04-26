package com.person.concurrent;


import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * countdownlatch和cyclibarrier的区别主要有两点：
 * ①重置②cyclicbarrier可以做额外的事情
 * @author huangchangling on 2018/1/5 0005
 */
//perform some portion of task
class TaskPortion implements Runnable {

    private static int counter = 0;

    private final int id = counter++;
    private static Random rand = new Random(47);
    private final CountDownLatch latch;

    TaskPortion(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            dowork();
            latch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void dowork() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(2000));
        System.out.println(this + " completed");
    }

    public String toString() {
        return String.format("%1$-3d", id);
    }
}

    //waits on the countDownLatch
   class WaitingTask implements Runnable{
        private static int counter = 0;
        private final int id = counter++;
        private final CountDownLatch latch;
        WaitingTask(CountDownLatch latch){
            this.latch = latch;
        }
        @Override
        public void run() {
            try {
                latch.await();
                System.out.println("Latch barrier passed for "+ this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        public String toString(){
            return String.format("WaitingTask %1$-3d",id);
        }
    }
    public class CountDownLatchDemo{
        static final int SIZE =100;
        public static void main(String[] args) {
            ExecutorService exec =Executors.newCachedThreadPool();
            CountDownLatch latch = new CountDownLatch(SIZE);
            for(int i=0;i<10;i++){
                exec.execute(new WaitingTask(latch));
            }
            for(int i=0;i<SIZE;i++){
                exec.execute(new TaskPortion(latch));
            }
            System.out.println("Launched all tasks");
            exec.shutdown();
        }
    }

