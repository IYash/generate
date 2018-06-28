package com.person.rpc.transport.nio;

/**
 * Created by huangchangling on 2018/6/28.
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        new Thread(new TimeClientHandle("127.0.0.1",port),"TimeClient-001").start();
    }
}
