package com.person.generate.dao;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by huangchangling on 2018/7/6.
 */
public class AtomicIntegerTest {
    CountDownLatch countDownLatch= new CountDownLatch(1);
    @Test
    public void testValue() throws Exception{
        AtomicInteger i = new AtomicInteger(5);
        System.out.println(i.get());
        //addValue(i);
        new Thread(new Task(i)).start();//将变量+1;
        countDownLatch.await();
        System.out.println(i.get());
    }

    private void addValue(AtomicInteger i) {
        i.getAndAdd(1);
    }
    class Task implements Runnable{
        AtomicInteger i;

        public Task(AtomicInteger i) {
            this.i = i;
        }

        @Override
        public void run() {
            i.getAndAdd(1);
            countDownLatch.countDown();
        }
    }
}
