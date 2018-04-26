package com.person.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author huangchangling on 2018/1/5 0005
 */
public class DelayedQueueDemo {
    public static void main(String[] args) {
        Random rand = new Random(47);
        ExecutorService exec = Executors.newCachedThreadPool();
        DelayQueue<DelayedTask> queue = new DelayQueue<>();
        for(int i=0;i<20;i++){
            queue.put(new DelayedTask(rand.nextInt(5000)));
        }
        queue.add(new DelayedTask.EndSentinel(5000,exec));
        exec.execute(new DelayedTaskConsumer(queue));
    }
}
class DelayedTask implements Runnable,Delayed{

    private static int counter=0;
    private final int id = counter++;
    private final int delta;
    private final long trigger;
    protected static List<DelayedTask> sequence = new ArrayList<>();
    DelayedTask(int delayMillionseconds){
        delta = delayMillionseconds;
        trigger = System.nanoTime() + TimeUnit.NANOSECONDS.convert(delta,TimeUnit.MILLISECONDS);
        sequence.add(this);
    }
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(trigger-System.nanoTime(),TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        DelayedTask that = (DelayedTask)o;
        if(trigger < that.trigger) return -1;
        if(trigger > that.trigger) return 1;
        return 0;
    }

    @Override
    public void run() {
        System.out.println(this + " ");
    }
    public String toString(){
        return String.format("[%1$-4d]",delta)+ " Task "+ id;
    }
    public String summary(){
        return "(" + id +":"+delta+")";
    }
    static class EndSentinel extends DelayedTask{
        private ExecutorService exec;
        EndSentinel(int delayMillionseconds,ExecutorService exec) {
            super(delayMillionseconds);
            this.exec = exec;
        }
        public void run(){
            for(DelayedTask pt:sequence){
                System.out.println(pt.summary()+" ");
            }
            System.out.println(this + " calling shutdownNow()");
            exec.shutdownNow();
        }
    }
}
class DelayedTaskConsumer implements Runnable{
    private DelayQueue<DelayedTask> q;

    public DelayedTaskConsumer(DelayQueue<DelayedTask> q) {
        this.q = q;
    }

    @Override
    public void run() {

            try {
                while(!Thread.interrupted())
                q.take().run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        System.out.println("finished DelayTask Consumer");
    }
}
