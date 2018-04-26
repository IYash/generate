package com.person.concurrent.pcdemo;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by huangchangling on 2018/4/25.
 * 自定义的共享资源.一个消息只消费一次的场景,不适合使用Condition
 */
public class ShareObject {

    private int seq;//共享标记，顺序递增
    private Lock lock = new ReentrantLock();
    private volatile boolean  flag = false;//是否被消费标记,若已消费，则退出
    private Condition condition = lock.newCondition();
    public void produce(){
        lock.lock();//保证内存可见性和操作原子性
        try{
            seq ++;
            flag = true;
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }
    public void consume(){
        lock.lock();
        try{
            while(!flag) condition.await();//占用线程资源，当先初始化消费者时，可能会耗尽线程资源谨慎使用
            System.out.println(seq+" -------------------found");
            flag = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }
}
