package com.person.generate.dao;

import org.junit.Test;

/**
 * Created by huangchangling on 2018/8/31.
 * 平滑的权重调用的一种实践
 */
public class CycleTest {

    //通过数组维护序号和权重的关系
    int[] weights = {4,4,2,1};
    int[] currentWs = new int[weights.length];
    @Test
    public void testCycle(){
        //将当前的值加倍后减去总权重,一个周期后权重恢复
        copyData(weights,currentWs);
        int total = 0;
        int max = 0;
        int len = weights.length;
        for(int w:weights) total += w;
        for(int i = 0;i<total;i++) {//外层的一个调用
            max = findMax(currentWs);
            boolean matched = false;
            for(int j=0;j<len;j++) {
                if ( !matched && currentWs[j] == max) {
                    System.out.println(j);
                    currentWs[j] = max - total + weights[j];
                    matched = true;
                }else {
                    currentWs[j] += weights[j];
                }
            }
        }
        System.out.println("============");
        for(int i=0;i<len;i++){
            System.out.println(currentWs[i]);
        }
    }
    int findMax(int[] src){
        int max = 0;
        for(int m:src) {
            max = m > max ? m : max;
        }
        return max;
    }
    void copyData(int[] src, int[] target) {
        for(int i=0;i<src.length;i++)
            target[i] = src[i];
    }
}
