package com.person.limit;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by huangchangling on 2018/5/24.
 * Hystrix是Netflix开源的一款容错框架，包含常用的容错方法：
 * 线程隔离、信号量隔离、降级策略、熔断技术。在高并发访问下，
 * 系统所依赖的服务的稳定性对系统的影响非常大，依赖有很多不可控的因素，
 * 比如网络连接变慢，资源突然繁忙，暂时不可用，服务脱机等。
 * 我们要构建稳定、可靠的分布式系统，就必须要有这样一套容错方法
 * 术语介绍：
 * Command:命令用于包裹对服务依赖或者外部系统的调用
 * CircuitBreaker:类似于现实世界的断路器，起保护系统的作用。能够在一段时间内停止调用某个或者某些服务
 * Fallback:命令执行出错时，系统可以使用fallback，从而实现优雅降级
 * Collapser:将若干命令合并成单一命令，可以减少调用次数
 */
public class HystrixCommandHW extends HystrixCommand<String>{

    private final String name;
    protected HystrixCommandHW(String name){
        //最少配置：指定命令组名
        super(HystrixCommandGroupKey.Factory.asKey("Example"));
        this.name = name;
    }
    @Override
    protected String run() throws Exception {
        //a real example would do work like a network call here
        //依赖逻辑封装在run()方法中
        return "hello "+ name + "! thread: " + Thread.currentThread().getName();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        /**
         * 每个Command对象只能调用一次，不可以重复调用
         * 重复调用对应的异常信息：This instance can only be executed once
         * please instantiate a new instance
         */
        HystrixCommandHW hw = new HystrixCommandHW("Synchronous-hystrix");
        //使用execute()同步调用代码，效果等同于：hw.queue().get();
        String s = hw.execute();
        System.out.println(" 同步 ========== "+s);
        //异步调用时，可以自由控制获取结果的时机
        hw = new HystrixCommandHW("Asynchronous-hystrix");
        Future<String> future = hw.queue();
        //get()操作不能超过command定义的超时时间，默认为1s
        s = future.get(100, TimeUnit.MICROSECONDS);
        System.out.println(" 异步 ============ "+s);

        System.out.println(" 主函数 =========== "+Thread.currentThread().getName());
        /**
         * 注意：
         * 异步调用使用command.queue().get(timeout,TimeUnit.MILLISECONDS);
         * 同步调用使用command.execute()等同于 command.queue().get();
         */
    }
}
