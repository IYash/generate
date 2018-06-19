package com.person.cache;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by huangchangling on 2018/6/19.
 * 带虚拟节点的一致性hash算法
 */
public class ConsistentHashingWithVN {
    /**
     * 待添加入Hash环的服务器列表
     */
    private static String[] servers = {"192.168.0.0:111", "192.168.0.1:111", "192.168.0.2:111","192.168.0.3:111", "192.168.0.4:111"};
    /**
     * 真实节点列表，考虑到服务器上线，下线的场景
     */
    private static List<String> realNodes = new LinkedList<>();
    /**
     * 虚拟节点，key标识虚拟节点的hash值，value标识虚拟节点的名称
     */
    private static SortedMap<Integer,String> vns = new TreeMap<>();
    /**
     * 虚拟节点的数目，这里写死，为了演示需要，一个真实节点对应5个虚拟节点
     */
    private static final int VIRTUAL_NODES = 5;
    static {
        //先把原始的服务器添加到真实节点列表中
        for(int i=0;i<servers.length;i++){
            realNodes.add(servers[i]);
        }
        //再添加虚拟节点，遍历linkedList使用foreach循环效率比较高
        for(String str:realNodes){
            for(int i=0;i<VIRTUAL_NODES;i++){
                String virtualNodeName = str+"&&VN"+String.valueOf(i);
                int hash = ConsistHashingUtil.getHash(virtualNodeName);
                System.out.println("虚拟节点["+virtualNodeName+"]被添加，hash值为"+hash);
                vns.put(hash,virtualNodeName);
            }
        }
        System.out.println();
    }



    /**
     * 得到对应的路由节点
     * @param node
     * @return
     */
    private static String getServer(String node){
        String serverName = ConsistHashingUtil.getServer(vns,node);
        return serverName.substring(0,serverName.indexOf("&&"));
    }

    public static void main(String[] args) {
        String[] nodes = {"127.0.0.1:1111", "221.226.0.1:2222", "10.211.0.1:3333"};
        for(int i=0;i<nodes.length;i++){
            System.out.println("["+nodes[i]+"]的hash值为"+ConsistHashingUtil.getHash(nodes[i])+",被路由到节点["+ConsistHashingUtil.getServer(vns,nodes[i])+"]");
        }
    }
}
