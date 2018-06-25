package com.person.cache;

import java.util.SortedMap;

/**
 * Created by huangchangling on 2018/6/19.
 */
public class ConsistHashingUtil {
    /**
     * 使用FNV1_32_HASH算法计算服务器的Hash值,这里不使用重写hashCode的方法，最终效果没区别
     * @param str
     * @return
     */
    public static int getHash(String str) {
        final int p = 16777619;
        int hash = (int)2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }

    /**
     * 得到应当路由到的节点
     * @param node
     * @return
     */
    public static String getServer(SortedMap<Integer,String> sortedMap,String node){
        //得到待路由节点的hash值
        int hash = ConsistHashingUtil.getHash(node);
        //得到大于该hash值的所有map
        SortedMap<Integer,String> subMap = sortedMap.tailMap(hash);
        //第一个key就是顺时针过去离node最近的那个节点
        Integer i = subMap.firstKey();
        //返回对应的服务器名称
        return subMap.get(i);
    }
}
