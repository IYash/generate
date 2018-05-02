package com.person.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * Created by huangchangling on 2018/5/2.
 * ThreadLocal,即线程变量，是一个以ThreadLocal对象为键、任意对象为值的存储结构
 */
public class Profiler {
    //第一次get()方法调用时会进行初始化（如果set方法没有调用），每个线程会调用一次
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<Long>(){
      protected Long initialValue(){
          return System.currentTimeMillis();
      }
    };
    public static final void begin(){
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }
    public static final long end(){
        return System.currentTimeMillis()-TIME_THREADLOCAL.get();
    }

    public static void main(String[] args) throws InterruptedException {
        Profiler.begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Cost: "+ Profiler.end()+" mills");
    }
}
