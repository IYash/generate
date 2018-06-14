package com.person.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by huangchangling on 2018/6/14.
 * delegation方式实现更加优雅一些，但是由于没有实现Map接口，
 * 所以线程同步需要自己完成
 */
public class LRUCache3<K,V> {
    private final int MAX_CACHE_SIZE;
    private final float DEFAULT_LOAD_FACTOR=0.75f;
    LinkedHashMap<K,V> map;

    public LRUCache3(int cacheSize) {
        this.MAX_CACHE_SIZE = cacheSize;
        //根据cacheSize和加载因子计算hashmap的capacity,
        //+1确保当达到cacheSize上限时不会触发hashmap扩容
        int capacity = (int)Math.ceil(MAX_CACHE_SIZE/DEFAULT_LOAD_FACTOR)+1;
        map = new LinkedHashMap<K,V>(capacity,DEFAULT_LOAD_FACTOR,true){
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest){
                return size() > MAX_CACHE_SIZE;
            }
        };
    }
    public synchronized void put(K key,V value){
        map.put(key,value);
    }
    public synchronized V get(K key){
        return map.get(key);
    }
    public synchronized void remove(K key){
        map.remove(key);
    }
    public synchronized Set<Map.Entry<K,V>> getAll(){
        return map.entrySet();
    }
    public synchronized int size(){
        return map.size();
    }
    public synchronized void clear(){
        map.clear();
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<K,V> entry:map.entrySet()){
            sb.append(String.format("%s:%s",entry.getKey(),entry.getValue()));
        }
        return sb.toString();
    }
}
