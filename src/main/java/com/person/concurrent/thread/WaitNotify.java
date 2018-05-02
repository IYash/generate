package com.person.concurrent.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by huangchangling on 2018/5/2.
 * 等待/通知机制依托于同步机制，其目的就是确保等待线程从
 * wait()方法返回时能够感知到通知线程对变量做出的修改
 */
public class WaitNotify {
    static boolean flag = true;
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread waitThread = new Thread(new Wait(),"waitThread");
        waitThread.start();
        TimeUnit.SECONDS.sleep(1);
        Thread notifyThread = new Thread(new Notify(),"notifyThread");
        notifyThread.start();
    }

    /**
     * synchronized(对象){
     *     while(条件不满足){对象.wait();}
     *     对应的处理逻辑
     * }
     */
    static class Wait implements Runnable{

        @Override
        public void run() {
            //加锁，拥有lock的Monitor
            synchronized (lock){
                //当条件不满足时，继续wait，同时释放了lock锁
                while(flag){
                    System.out.println(Thread.currentThread()+"" +
                            " flag is true.wait @ "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
                    try {
                        //需要先对调用对象加锁
                        lock.wait();//线程状态由RUNNING变为WAITING,并将当前线程放置到对象的等待队列，从wait()返回的前提是获得了调用对象的锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //当条件满足时，完成工作
                System.out.println(Thread.currentThread()+" flag is false.running @ "+new SimpleDateFormat(
                        "HH:mm:ss"
                ).format(new Date()));
            }
        }
    }

    /**
     * synchronized(对象){
     *     改变条件
     *     对象.notifyAll();
     * }
     */
    static class Notify implements Runnable{

        @Override
        public void run() {
            //加锁，拥有lock的Monitor
            synchronized (lock){
                //获取lock锁，然后进行通知，通知时不会释放lock的锁
                //知道当前线程释放了lock后，waitThread才能从wait方法中返回
                System.out.println(Thread.currentThread()+" hold lock.notify @ "+ new SimpleDateFormat(
                        "HH:mm:ss"
                ).format(new Date()));
                lock.notifyAll();//等待线程依旧不会从wait()返回，需要调用的线程释放锁之后，等待线程才有机会从wait()返回
                flag = false;
                SleepUtils.second(5);
            }
            //再次加锁
            synchronized (lock){
                System.out.println(Thread.currentThread() + " hold lock again.sleep @ "+new SimpleDateFormat(
                        "HH:mm:ss"
                ).format(new Date()));
                SleepUtils.second(5);
            }
        }
    }
}
