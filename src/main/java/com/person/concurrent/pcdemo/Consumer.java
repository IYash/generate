package com.person.concurrent.pcdemo;

import java.util.concurrent.BlockingQueue;

/**
 * Created by huangchangling on 2018/4/25.
 * 当消费者不知道生产者的存在时，只关注自己订阅的消息
 * 通过BlockingQueue模拟订阅的容器
 */
public class Consumer implements Runnable{

    private BlockingQueue<Message> bq;
    public Consumer(BlockingQueue bq){
        this.bq = bq;
    }
    public Consumer(ShareObject object){this.object = object;}
    private ShareObject object;

    @Override
    public void run() {
        if(bq != null) consumeThoughQueue();
        else
            consumeThoughShareObject();
    }
    private void consumeThoughShareObject(){
        object.consume();
        System.out.println();
    }
    private void consumeThoughQueue(){
        Message  m = bq.poll();//若是自定义的其他资源，则需考虑线程的安全问题
        if (m!=null)
            System.out.println(Thread.currentThread()+" consume message "+m.getMessage());
    }
    }

