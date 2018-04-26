package com.person.concurrent.pcdemo;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * Created by huangchangling on 2018/4/25.
 * 生产者只管生产
 */
public class Producer implements Runnable {

    private BlockingQueue<Message> bq ;

    public Producer(BlockingQueue<Message> bq) {
        this.bq = bq;
    }

    public Producer(ShareObject object) {
        this.object = object;
    }

    private ShareObject object;
    @Override
    public void run() {
          if (bq!=null) produceThoughQueue();
        else
              productThoughShareObject();
    }

    private void productThoughShareObject(){
        object.produce();
        System.out.println();
    }
    private void produceThoughQueue(){
        Message m  = Message.messageBuilder().buildMessage("info "+new Random().nextInt());
        bq.offer(m);//若是自定义的，则需要保证线程的安全
        System.out.println(Thread.currentThread().getName() + " produce message "+m.getMessage());
    }
}
