package com.person.rpc.transport.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by huangchangling on 2018/6/28.
 */
public class TimeClient {
    public static void main(String[] args) throws Exception{
        TimeClient client = new TimeClient();
        client.requestTime();
    }
    public void requestTime() throws Exception{
        int port = 8080;
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket("127.0.0.1",port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            out.println("QUERY TIME ORDER");
            System.out.println("Send order 2 server succeed.");
            String resp = in.readLine();
            System.out.println("Now is :"+ resp);
        } catch (IOException e) {
            e.printStackTrace();
            if(in !=null) try {
                in.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if(out != null) out.close();
            if(socket != null) try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
