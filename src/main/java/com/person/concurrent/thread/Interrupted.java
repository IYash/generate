package com.person.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * Created by huangchangling on 2018/5/2.
 * 中断可以理解为线程的一个标识位属性，它标识一个运行中的线程是否被其他线程进行了
 * 中断操作，其他线程通过调用该线程的interrupt()方法对其进行中断操作
 * 线程通过检查自身是否被中断来进行响应，线程通过isInterrupted()来进行判断是否被中断
 * 许多声明抛出InterruptedException的方法，这些方法在抛出InterruptedException之前，java
 * 虚拟机会将该线程的中断标识位清除
 */
public class Interrupted {
    public static void main(String[] args) throws InterruptedException {
        //sleepThread不停的尝试睡眠
        Thread sleepThread = new Thread(new SleepRunner(),"sleepThread");
        sleepThread.setDaemon(true);
        //busyThread不停的运行
        Thread busyThread = new Thread(new BusyRunner(),"busyThread");
        busyThread.setDaemon(true);
        sleepThread.start();
        busyThread.start();
        //休眠5秒，让sleepThread和busyThread充分运行
        TimeUnit.SECONDS.sleep(5);
        sleepThread.interrupt();
        busyThread.interrupt();
        System.out.println("SleepThread interrupted is "+ sleepThread.isInterrupted());
        System.out.println("BusyThread interrupted is "+ busyThread.isInterrupted());
        //防止sleepThread和busyThread立刻退出
        SleepUtils.second(2);
    }
    static class SleepRunner implements Runnable{

        @Override
        public void run() {
            while(true) SleepUtils.second(10);
        }
    }
    static class BusyRunner implements Runnable{

        @Override
        public void run() {
            while(true){}
        }
    }
}
