package com.person.limit;

import com.netflix.hystrix.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by huangchangling on 2018/5/24.
 */
public class HystrixCommandHW1 extends HystrixCommand<String> {

    private final String name;
    protected HystrixCommandHW1(String name){
        super(//命令分组用于对依赖操作分组，便于统计，汇总等.
                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HW"))
                //配置依赖超时时间500ms
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                .withExecutionTimeoutInMilliseconds(500))
                //HystricCommandKey工厂定义依赖名称
                .andCommandKey(HystrixCommandKey.Factory.asKey("HW1"))
                //使用HystrixThreadPoolKey工厂定义线程池名称
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("HWPool"))
        );
        this.name = name;
    }
    @Override
    protected String getFallback(){
        return "execute Failed";
    }
    @Override
    protected String run() throws Exception {
        //sleep 1s,调用会超时
        TimeUnit.MILLISECONDS.sleep(1000);
        return "hello "+name+" thread : "+Thread.currentThread().getName();
    }

    public static void main(String[] args) {
        HystrixCommandHW1 hw1 = new HystrixCommandHW1("test-Fallback");
        String s = hw1.execute();
        System.out.println(" 同步 ======= "+s);
        /**
         * 注意：
         * 1.除了HystrixBadRequestException异常之外，所有从run()方法抛出的异常都算作失败，
         并触发降级getFallback()和断路器逻辑。

         2.HystrixBadRequestException用在非法参数或非系统故障异常等不应触发回退逻辑的场景。

         3.每个CommandKey代表一个依赖抽象,相同的依赖要使用相同的CommandKey名称。
         依赖隔离的根本就是对相同CommandKey的依赖做隔离.

         4.CommandGroup是每个命令最少配置的必选参数，
         在不指定ThreadPoolKey的情况下，字面值用于对不同依赖的线程池/信号区分

         5.当对同一业务依赖做隔离时使用CommandGroup做区分,
         但是对同一依赖的不同远程调用如(一个是redis 一个是http),
         可以使用HystrixThreadPoolKey做隔离区分.
         最然在业务上都是相同的组，但是需要在资源上做隔离时，
         可以使用HystrixThreadPoolKey区分.

         6.以下四种情况将触发getFallback调用：
         1.)run()方法抛出非HystrixBadRequestException异常。
         2.)run()方法调用超时
         3.)熔断器开启拦截调用
         4.)线程池/队列/信号量是是否跑满
         实现getFallback()后,执行命令时遇到以上4中情况将被fallback接管,
         不会抛出异常或其他。
         */
    }
}
