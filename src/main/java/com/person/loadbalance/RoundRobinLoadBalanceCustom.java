package com.person.loadbalance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by huangchangling on 2018/6/25.
 * 负载均衡的一种实现方式：轮询
 * 轮询调度算法的原理是每一次把来自用户的请求轮流分配给内部中的服务器
 * 从1开始，知道N（内部服务器个数），然后重新开始循环，是一种无状态调度
 * 结合服务器的权重实现（服务器性能高的可以适当分配比较大的权重）
 */
public class RoundRobinLoadBalanceCustom implements LoadBalance<ServiceProvider>{

    //记录每个serviceProvider调用的次数
    private final ConcurrentHashMap<String,AtomicInteger> sequences = new ConcurrentHashMap<>();//用于记录currentIndex
    private final ConcurrentHashMap<String,AtomicInteger> weightSequences = new ConcurrentHashMap<>();//用于记录权重的序列
    @Override
    public ServiceProvider doSelect(List<ServiceProvider> providers) {
        String key = providers.get(0).getKey();//同一个服务列表提供的服务是一样的
        int length = providers.size();//总个数
        int maxWeight = 0;//最大权重
        int minWeight = Integer.MAX_VALUE;//最小权重
        int totalWeight = 0;
        for(int i = 0;i<length;i++){
            int weight = providers.get(i).getServiceWeight();
            maxWeight = Math.max(maxWeight,weight);//累计最大权重
            minWeight = Math.min(minWeight,weight);//累计最小权重
            totalWeight += weight;
        }
        if(maxWeight >0 && minWeight < maxWeight) {//权重不一样
            AtomicInteger weightSequence = weightSequences.get(key);
            if (weightSequence == null) {
                weightSequences.putIfAbsent(key, new AtomicInteger());
                weightSequence = weightSequences.get(key);
            }
            int currentWeight = weightSequence.getAndIncrement() % totalWeight;
            int weight = 0;
            for (ServiceProvider p : providers) {//筛选权重大于当前权重基数的provider
                weight += p.getServiceWeight();
                if (weight > currentWeight) return p;
            }
        }
        AtomicInteger sequence = sequences.get(key);
        if(sequence == null){
            sequences.putIfAbsent(key,new AtomicInteger());
            sequence = sequences.get(key);
        }
        return providers.get(sequence.getAndIncrement()%length);
    }

    public static void main(String[] args) {
        RoundRobinLoadBalanceCustom balance = new RoundRobinLoadBalanceCustom();
        //初始化服务
        List<ServiceProvider> ps = balance.init("123",4);
        //服务调用
        for(int i=0;i<40;i++){
            ServiceProvider  s = balance.doSelect(ps);
            System.out.println(s.getServiceName());
        }

    }
    private List<ServiceProvider> init(String key,int count){
        List<ServiceProvider> providers = new ArrayList<>();
        for(int i=1;i<=count;i++){
            ServiceProvider s = new ServiceProvider();
            s.setKey(key);
            s.setServiceWeight(i);
            s.setServiceName("s"+i);
            providers.add(s);
        }
        return providers;
    }
}
