package com.person.rpc.transport.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by huangchangling on 2018/6/28.
 */
public class TimeServer {
    public static void main(String[] args) throws Exception{
        int port = 8080;
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port:"+port);
            Socket socket = null;
            while(true){
                socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();//可以通过线程实现伪异步处理，伪异步可以保证资源在有限范围内，但有可能一起引起服务奔溃的问题，当线程池线程耗尽的时候
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(server!=null){
                System.out.println("The time server close");
                server.close();
                server = null;
            }
        }
    }
}
