package com.person.limit;

import com.netflix.hystrix.*;

/**
 * Created by huangchangling on 2018/5/24.
 */
public class ApiSetter {
    public static HystrixCommand.Setter setter(String commandKeyName, String threadPoolKeyName) {
        return setter("ApiGroup",commandKeyName,threadPoolKeyName);
    }

    public static HystrixCommand.Setter setter(String commandKeyName) {
        return setter(commandKeyName,"Api-Pool");
    }

    /**
     * @author liweihan
     * @time 2017/12/20 16:57
     * @description     相关参数设置
     * @param groupKeyName      服务分组名
     * @param commandKeyName    服务标识名称
     * @param threadPoolKeyName 线程池名称
     * @return
     */
    public static HystrixCommand.Setter setter(String groupKeyName, String commandKeyName, String threadPoolKeyName) {
        //服务分组
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey(groupKeyName);
        //服务标识
        HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey(commandKeyName);
        //线程池名称
        HystrixThreadPoolKey threadPoolKey = HystrixThreadPoolKey.Factory.asKey(threadPoolKeyName);
        //线程配置
        HystrixThreadPoolProperties.Setter threadPoolProperties = HystrixThreadPoolProperties.Setter()
                .withCoreSize(25)
                .withKeepAliveTimeMinutes(5)
                .withMaxQueueSize(Integer.MAX_VALUE)
                .withQueueSizeRejectionThreshold(10000);

        //命令属性的配置
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter()
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                .withExecutionIsolationThreadInterruptOnTimeout(true)
                .withExecutionTimeoutInMilliseconds(3000) //设置超时时间为3秒时自动熔断
                .withCircuitBreakerErrorThresholdPercentage(20);//失败率达到20%自动熔断

        //返回
        return HystrixCommand.Setter
                .withGroupKey(groupKey)
                .andCommandKey(commandKey)
                .andThreadPoolKey(threadPoolKey)
                .andThreadPoolPropertiesDefaults(threadPoolProperties)
                .andCommandPropertiesDefaults(commandProperties);
    }

    /**
     * ☆参数说明：
     1.HystrixCommandGroupKey：服务分组，以上groupKey分组就包括多个服务，必填选项

     2.HystrixCommandKey：服务的名称，唯一标识，如果不配置，则默认是类名

     3.HystrixThreadPoolKey：线程池的名称，相同线程池名称的线程池是同一个，如果不配置，默认为分组名

     4.HystrixThreadPoolProperties：线程池的配置，
     coreSize配置核心线程池的大小，
     maxQueueSize线程池队列的最大大小，
     queueSizeRejectionThreshold，限制当前队列的大小，
     实际队列大小由这个参数决定，即到达队列里面条数到达10000，则都会被拒绝。

     5.HystrixCommandProperties：配置命令的一些参数，
     如executionIsolationStrategy，配置执行隔离策略，默认是使用线程隔离，THREAD即为线程池隔离，

     ExecutionIsolationThreadInterruptOnTimeout 使用线程隔离时,是否对命令执行超时的线程调用中断操作.默认：true
     和ExecutionTimeoutInMilliseconds配置了启用超时和最大执行时间，这里为3s，

     circuitBreakerErrorThresholdPercentage失败率配置，默认为50%，
     这里配置的为25%，即失败率到达25%触发熔断
     */

}
