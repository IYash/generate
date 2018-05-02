package com.person.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * Created by huangchangling on 2018/5/2.
 */
public class SleepUtils {
    public static final void second(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
