package com.person.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by huangchangling on 2018/6/14.
 * 采用inheritance方式实现，而且实现了Map接口，在多线程环境使用时可以使用
 * Collections.synchronizedMap()方法实现线程安全操作
 */
public class LRUCache2<K,V> extends LinkedHashMap<K,V> {
    private final int MAX_CACHE_SIZE;

    public LRUCache2(int cacheSize) {
        super((int)Math.ceil(cacheSize/0.75)+1,0.75f,true);
        this.MAX_CACHE_SIZE = cacheSize;
    }
    @Override
    public boolean removeEldestEntry(Map.Entry eldest){
        return size() > MAX_CACHE_SIZE;
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<K,V> entry:entrySet()){
            sb.append(String.format("%s:%s",entry.getKey(),entry.getValue()));
        }
        return sb.toString();
    }
}
