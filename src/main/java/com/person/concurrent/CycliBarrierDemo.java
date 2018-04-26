package com.person.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author huangchangling on 2018/1/5 0005
 */
public class CycliBarrierDemo {
    public static void main(String[] args) {
        int nHorses = 7;
        int pause =200;
        new HorseRace(nHorses,pause);
    }
}
class Horse implements Runnable{
    private static int counter=0;
    private final int id = counter++;
    private int strides = 0;
    private static Random rand = new Random(47);
    private static CyclicBarrier barrier;
    Horse(CyclicBarrier barrier){
        this.barrier = barrier;
    }
    public synchronized int getStrides(){return strides;}
    @Override
    public void run() {
            try {
                while(!Thread.interrupted()) {
                    synchronized (this) {
                        strides += rand.nextInt(3);//produce 0,1,2
                    }
                    barrier.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    public String toString(){
        return "Horse "+ id +" ";
    }
    public String tracks(){
        StringBuilder s  = new StringBuilder();
        for(int i=0;i<getStrides();i++)
            s.append("*");
        s.append(id);
        return s.toString();
    }
}
    class HorseRace{
        static final int FINISH_LINE = 75;
        private List<Horse> horses = new ArrayList<>();
        private ExecutorService exec= Executors.newCachedThreadPool();
        private CyclicBarrier barrier;
        HorseRace(int nHorses,final int pause){
            barrier = new CyclicBarrier(nHorses, new Runnable() {
                @Override
                public void run() {
                    StringBuilder s = new StringBuilder();
                    for(int i=0;i<FINISH_LINE;i++)
                        s.append("=");
                    System.out.println(s.toString());
                    for(Horse horse:horses)
                        System.out.println(horse.tracks());
                    for (Horse horse:horses)
                        if(horse.getStrides() >= FINISH_LINE){
                            System.out.println(horse + " won!");
                            exec.shutdownNow();//interrupt
                            return;
                        }
                    try {
                        TimeUnit.MILLISECONDS.sleep(pause);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            for(int i=0;i<nHorses;i++) {
                    Horse horse = new Horse(barrier);
                    horses.add(horse);
                    exec.execute(horse);
                }
        }
    }
