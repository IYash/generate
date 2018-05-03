package com.person.concurrent.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by huangchangling on 2018/5/3.
 * 有界队列是一种特殊的队列，当队列为空时，队列的获取操作
 * 将会阻塞获取线程，知道队列中有新增元素，当队列已满时，队列的
 * 操作操作将会阻塞插入线程，知道队列出现“空位”
 */
public class BoundedQueue<T> {
    private Object[] items;
    //添加的下标，删除的下标和数组当前数量
    private int addIndex,removeIndex,count;
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();
    public BoundedQueue(int size){
        items = new Object[size];
    }
    //添加一个元素，如果数组满，则添加线程进入等待状态，知道有“空位”
    public void add(T t) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) notFull.await();
            items[addIndex] = t;
            if (++addIndex == items.length) addIndex = 0;
            ++count;
            notEmpty.signal();
        }finally{
            lock.unlock();
        }
    }
    //由头部删除一个元素，如果数组空，则删除线程进入等待状态，直到有新添加元素
    public T remove() throws InterruptedException {
        //获得锁，目的是确保数组修改的可见性和排他性
        lock.lock();
        try {
            //使用while循环而非if判断，目的是防止过早或意外的通知
            while (count == 0) notEmpty.await();
            Object x = items[removeIndex];
            if (++removeIndex == items.length) removeIndex = 0;
            --count;
            notFull.signal();
            return (T) x;
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BoundedQueue<String> bq = new BoundedQueue<>(2);
        bq.add("hello");
        bq.add("world");
        bq.add("haha");//线程会一直处于等待状态
        bq.remove();
        System.out.println(bq.count);
    }
}
