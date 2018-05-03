package com.person.concurrent.blockingqueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by huangchangling on 2018/5/3.
 */
public class SynchronousQueueTest {
    //线程池其实是一个消费者线程
    //private static ExecutorService cachedExecutor = Executors.newCachedThreadPool();
    private static SynchronousQueue queue = new SynchronousQueue();
    public static void main(String[] args) {
        for (int i=0;i<1;i++) {
            Task task = new Task();
            //cachedExecutor.execute(task);
            addTask(task);
        }
        System.out.println("end==============");
    }

    private static void addTask(Task task){
        //cachedExecutor.execute(task);
        boolean ret = queue.offer(task);
        System.out.println(ret);
    }
    private static  class Task implements Runnable{
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()+"hello world");
        }
    }
}
