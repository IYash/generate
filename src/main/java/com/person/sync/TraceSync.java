package com.person.sync;

import java.util.concurrent.CountDownLatch;

/**
 * @author huangchangling on 2018/2/11 0011
 * synchronized的关键字使用的内部逻辑,线程状态
 */
public class TraceSync {

    private int scale;
    public final CountDownLatch latch ;
    public TraceSync(int scale) {
        this.scale = scale;
        latch = new CountDownLatch(scale);
    }

    /**
     * 场景描述：A,B线程同时获取一个互斥锁,同步方法方式
     *
     * @throws InterruptedException
     */
    public synchronized void getLock(){//BLOCKED(on object monitor)
       lockBody();
    }
    public static synchronized  void parse(){
        System.out.println("1234");
    }
    public void getBodyLock(){
        synchronized (this){//BLOCKED(on object monitor)
            lockBody();
        }
    }
    public void lockBody(){
        //通过sleep 10s模拟资源占用时间
        try {
            System.out.println("get lock ======================"+Thread.currentThread().getName());
            Thread.sleep(5000);//TIMED_WAITING(sleeping) at java.lang.Thread.sleep(Native Method),不释放资源
            this.wait(100000);//TIMED_WAITING(on object monitor) at java.lang.Object.wait(Native Method)，释放资源
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        int scale = 5;
        final TraceSync source = new TraceSync(scale);
        //使用CountDownLatch进行并发模拟，手动创建线程
        for(int i=0;i<scale;i++){
            source.latch.countDown();
            //这里可以使用线程池加以替换，会更加优雅，需要在任务的run完成并发的控制
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        source.latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    new LocalJob(source).run();
                }
            }).start();
        }

    }

}
    class LocalJob implements Runnable{
        private TraceSync source ;

        public LocalJob(TraceSync source) {
            this.source = source;
        }

        @Override
        public void run() {
        System.out.println("begin to work======================"+Thread.currentThread().getName());
        //source.getLock();
        source.getBodyLock();
        }
}
