package com.person.limit;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/3/30.
 * 令牌桶 限流
 */
public class ApiCallDemo {

    private int permitsPerSecond = 4;//每秒10个许可
    private int threadNum = 3;

    public static void main(String[] args) {
        new ApiCallDemo().call();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("end....");
            }
        }));
    }
    private void call(){
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        final RateLimiter rateLimiter = RateLimiter.create(permitsPerSecond);
        for(int i=0;i<threadNum;i++){
            executor.execute(new ApiCallTask(rateLimiter));
        }
        executor.shutdown();
    }
    class ApiCallTask implements Runnable{
        private RateLimiter rateLimiter;

        public ApiCallTask(RateLimiter rateLimiter) {
            this.rateLimiter = rateLimiter;
        }

        @Override
        public void run() {
            rateLimiter.acquire(10);//or rateLimiter.tryAcquire()
            getData();
        }
    }
    //模拟调用合作伙伴api接口
    private void getData(){
        System.out.println(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss")+","+Thread.currentThread().getName()+" RUNNING!");
        try {
            TimeUnit.MICROSECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
