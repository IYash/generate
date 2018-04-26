package com.person.concurrent.pcdemo;


import java.util.concurrent.*;

/**
 * Created by huangchangling on 2018/4/25.
 */
public class PCDemo {

    public static void main(String[] args) throws Exception{
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(new Monitor(2,5));
        System.out.println("PRESS 'ENTER' TO QUIT");
        System.in.read();
        executor.shutdown();
    }
}
