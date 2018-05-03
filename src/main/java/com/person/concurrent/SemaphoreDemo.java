package com.person.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by huangchangling on 2018/5/3.
 * 用来控制同时访问特定资源的线程数量，它通过协调各个线程，
 * 以保证合理的使用公共资源
 */
public class SemaphoreDemo {
    private static final int THREAD_COUNT=30;
    private static ExecutorService exec=Executors.newFixedThreadPool(THREAD_COUNT);
    private static Semaphore s = new Semaphore(10);

    public static void main(String[] args) {
        for(int i=0;i<THREAD_COUNT;i++){
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        s.acquire();
                        System.out.println("save data");
                        s.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        exec.shutdown();
    }
}
