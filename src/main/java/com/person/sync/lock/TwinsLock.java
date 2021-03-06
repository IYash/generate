package com.person.sync.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by Administrator on 2018/4/12.
 */
public class TwinsLock implements Lock {
    private final Sync sync = new Sync(2);
    private static final class Sync extends AbstractQueuedSynchronizer{
        Sync(int count){
            if(count <= 0) throw new IllegalArgumentException("count must larger than zero");
            setState(count);
        }
        public int tryAcquireShared(int reduceCount){
            for(;;){
                int current = getState();
                int newCount = current - reduceCount;
                if(newCount <0 || compareAndSetState(current,newCount)){
                    return newCount;
                }
            }
        }
        public boolean tryReleaseShared(int reduceCount){
            for(;;){
                int current = getState();
                int newCount = current+reduceCount;
                if(compareAndSetState(current,newCount)) return true;
            }
        }
    }
    @Override
    public void lock() {
        sync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    public static void main(String[] args) throws InterruptedException {
            final Lock lock = new TwinsLock();
            class Worker extends Thread {
                public void run() {
                    while (true) {
                        lock.lock();
                        try {
                            try {
                                Thread.sleep(1000);
                                System.out.println(Thread.currentThread().getName());
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        } finally {
                            lock.unlock();
                        }
                    }
                }
            }
// 启动10个线程
            for (int i = 0; i < 10; i++) {
                Worker w = new Worker();
                w.setDaemon(true);
                w.start();
            }
// 每隔1秒换行
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                System.out.println();
            }

        }
}
