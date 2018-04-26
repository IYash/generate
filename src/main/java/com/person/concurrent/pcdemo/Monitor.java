package com.person.concurrent.pcdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by huangchangling on 2018/4/25.
 * 监听器，用于调整生产者和消费者数量
 * 当前这个模拟中存在的一个问题是所有的消费者，生产者共享同一个容器中的资源
 */
public class Monitor implements Runnable {

    private List<Producer> producers = new ArrayList<>();
    private List<Consumer> consumers = new ArrayList<>();
    private BlockingQueue bq = new LinkedBlockingQueue();//若将bq替换程其他类型的资源，则需要通过锁来保证线程的安全
    private Executor exe = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private Random r = new Random();
    private ShareObject object = new ShareObject();
    public Monitor(int np,int nc) {
        for(int i=0;i<np;i++) {
            Producer p = new Producer(object);//可替换资源
            producers.add(p);
        }
        for(int i=0;i<nc;i++) {
            Consumer c = new Consumer(object);//可替换资源
            consumers.add(c);
        }
    }

    @Override
    public void run() {
        while(!Thread.interrupted()){
            int ci = r.nextInt(consumers.size());
            System.out.println(ci);
            if (ci >0){
                int pi = r.nextInt(producers.size());
                System.out.println(pi);
                exe.execute(producers.get(pi));
                exe.execute(consumers.get(ci));
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
