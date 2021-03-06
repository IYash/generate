package com.person.loadbalance;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangchangling on 2018/6/25.
 * nginx的负载均衡-加权轮询了解一下
 * https://blog.csdn.net/zhangskd/article/details/50194069
 */
public class WeightRoundRobin {

    /**
     * 上次选择的服务器
     */
    private int currentIndex = -1;
    /**
     * 当前调度的权值
     */
    private int currentWeight = 0;
    /**
     * 最大权重
     */
    private int maxWeight;
    /**
     * 权重的最大公约数
     */
    private int gcdWeight;
    /**
     * 服务器数
     */
    private int serverCount;
    private List<Server> servers = new ArrayList<>();

    public int greaterCommonDivisor(int a, int b) {
        BigInteger aBig = new BigInteger(String.valueOf(a));
        BigInteger bBig = new BigInteger(String.valueOf(b));
        return aBig.gcd(bBig).intValue();
    }

    public int greatestCommonDivisor(List<Server> servers) {
        int divisor = 0;
        for (int index = 0, len = servers.size(); index < len - 1; index++) {
            if (index == 0) {
                divisor = greaterCommonDivisor(
                        servers.get(index).getWeight(), servers.get(index + 1).getWeight());
            } else {
                divisor = greaterCommonDivisor(divisor, servers.get(index).getWeight());
            }
        }
        return divisor;
    }

    public int greatestWeight(List<Server> servers) {
        int weight = 0;
        for (Server server : servers) {
            if (weight < server.getWeight()) {
                weight = server.getWeight();
            }
        }
        return weight;
    }

    /**
     * 算法流程：
     * 假设有一组服务器 S = {S0, S1, …, Sn-1}
     * 有相应的权重，变量currentIndex表示上次选择的服务器
     * 权值currentWeight初始化为0，currentIndex初始化为-1 ，当第一次的时候返回 权值取最大的那个服务器，
     * 通过权重的不断递减 寻找 适合的服务器返回，直到轮询结束，权值返回为0
     */
    public Server getServer() {
        while (true) {
            currentIndex = (currentIndex + 1) % serverCount;
            if (currentIndex == 0) {
                currentWeight = currentWeight - gcdWeight;//按照公约后设置轮询的比率
                if (currentWeight <= 0) {
                    currentWeight = maxWeight;
                    if (currentWeight == 0) {
                        return null;
                    }
                }
            }
            if (servers.get(currentIndex).getWeight() >= currentWeight) {
                return servers.get(currentIndex);
            }
        }
    }

    public void init() {
        servers.add(new Server("192.168.1.101", 2));
        servers.add(new Server("192.168.1.102", 2));
        servers.add(new Server("192.168.1.103", 4));
        servers.add(new Server("192.168.1.104", 4));
        servers.add(new Server("192.168.1.105", 8));

        maxWeight = greatestWeight(servers);
        gcdWeight = greatestCommonDivisor(servers);
        serverCount = servers.size();
    }

    public static void main(String[] args) {
        WeightRoundRobin weightRoundRobin = new WeightRoundRobin();
        weightRoundRobin.init();

        for (int i = 0; i < 15; i++) {
            Server server = weightRoundRobin.getServer();
            System.out.println("server " + server.getIp() + " weight=" + server.getWeight());
        }
    }

    class Server {
        private String ip;

        private int weight;

        public Server(String ip, int weight) {
            this.ip = ip;
            this.weight = weight;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;

            if (obj instanceof Server) {
                Server server = (Server) obj;

                return getIp().equals(server.getIp());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return getIp().hashCode();
        }

    }
}

