package com.person.forkjoin;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/3/23.
 */
public class MulThreadTest {
    private  static ExecutorService exec = Executors.newFixedThreadPool(5);
    public static void main(String[] args) {
       for (int i=0;i<5;i++){
           Task task = new Task();
           exec.submit(task);
       }
        System.out.println("over");
    }
    static class Task implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("=============");
        }
    }
}
