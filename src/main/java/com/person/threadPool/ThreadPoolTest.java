package com.person.threadPool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by huangchangling on 2018/5/19.
 * 用于测试自定义的线程池
 * 测试目标：
 * 查看线程池的运行效率,提交的任务数，运行的任务数
 */
public class ThreadPoolTest {

    private static CyclicBarrier cb ;
    private static CountDownLatch latch;
    public static void main(String[] args) {
        ThreadPool<Job> pool = new CustomizeThreadPool<>(3,4,100);
        //模拟并发
        int count = 5;
        latch = new CountDownLatch(count+1);
        cb = new CyclicBarrier(count, new Runnable() {
            @Override
            public void run() {
                System.out.println("execute job end================");
                latch.countDown();
            }
        });
        //模拟线程，用于创建任务
        for(int i=0;i<count;i++){
            Thread mock = new Thread(new MockTask(pool,21));
            mock.start();
        }
        //主线程等待，统计结果
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end...");
    }
    //创建一个用于模拟并发的任务
    static class MockTask implements Runnable{
        int tasks ;
        ThreadPool pool;
        public MockTask(ThreadPool pool,int tasks) {
            this.tasks = tasks;
            this.pool = pool;
        }
        @Override
        public void run() {
            try {
                cb.await();
                while(tasks>0){
                    pool.execute(new Job());
                    tasks -- ;
                }
               latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //提交到线程池的任务
    static class Job implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(100);//模拟任务执行时间
                System.out.println("====================="+Thread.currentThread().getName()+" work done");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
