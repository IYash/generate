package com.person.cache;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by huangchangling on 2018/6/19.
 * 不带虚拟节点的一致性hash算法
 */
public class ConsistentHashingWithoutVN {
    /**
     * 待添加入Hash环的服务器列表
     */
    private static String[] servers = {"192.168.0.0:111", "192.168.0.1:111", "192.168.0.2:111","192.168.0.3:111", "192.168.0.4:111"};
    /**
     * key为服务器的hash值，value标识服务器的名称
     */
    private static SortedMap<Integer,String> sortedMap = new TreeMap<>();

    /**
     * 程序初始化，将所有的服务器放入sortedMap中
     */
    static{
        for(int i=0;i<servers.length;i++){
            int hash = ConsistHashingUtil.getHash(servers[i]);
            System.out.println("["+servers[i]+"]加入集合中，其hash值为"+hash);
            sortedMap.put(hash,servers[i]);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        String[] nodes = {"127.0.0.1:1111", "221.226.0.1:2222", "10.211.0.1:3333"};
        for(int i=0;i<nodes.length;i++){
            System.out.println("["+nodes[i]+"]的hash值为"+ConsistHashingUtil.getHash(nodes[i])+",被路由到节点["+ConsistHashingUtil.getServer(sortedMap,nodes[i])+"]");
        }
    }
}
