package com.person.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * Created by huangchangling on 2018/6/25.
 * 负载均衡的一种实现：基于权值进行进行分发
 * 概率通过随机的区间实现
 * 一致性hash的实现方式参考cache的写法
 */
public class RandomLoadBalance implements LoadBalance<ServiceProvider>{

    //负载均衡处理逻辑，从多个提供者中选择出一个
    @Override
    public ServiceProvider doSelect(List<ServiceProvider> providers){
        boolean sameWeight = true;
        int totalWeight = 0;
        int currentWeight = 0;
        int len = providers.size();
        for (int i=0;i<len;i++) {
            currentWeight = providers.get(i).getServiceWeight();
            totalWeight += currentWeight;
            //这个判断逻辑比较高明，只要存在任意相邻的两个权值不一致时，整体权值就是不一致的
            if (sameWeight && i>0 && currentWeight != providers.get(i-1).getServiceWeight())
                sameWeight = false;
        }
        //用于模拟随机数
        Random random = new Random();
        //当权重不一致时，根据权重随机选择
        if(!sameWeight && totalWeight > 0){
            //遍历提供提供列表，当offset<0时，即为服务提供者
            int offset = random.nextInt(totalWeight);
            for(int i=0;i<len;i++){
                offset -= providers.get(i).getServiceWeight();
                if(offset < 0)
                    return providers.get(i);
            }
        }
        return providers.get(random.nextInt(len));
    }
}
