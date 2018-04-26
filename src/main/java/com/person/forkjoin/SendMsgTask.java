package com.person.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Created by Administrator on 2018/3/23.
 */
public class SendMsgTask extends RecursiveAction {
    private List<String> msgList;

    private static final Integer THRESHOLD = 10;

    SendMsgTask(List msgList){
        this.msgList = msgList;
    }

    @Override
    protected void compute() {
        int size = msgList.size();
        boolean needMultiCount = false;
        System.out.println(needMultiCount);
        if(needMultiCount){
            int middle = size/2;
            System.out.println("=====?"+size);
            List part1 = new ArrayList();
            List part2 = new ArrayList();

            part1.addAll(msgList.subList(0,middle));
            part2.addAll(msgList.subList(middle,msgList.size()));

            System.out.println("====="+size);

            SendMsgTask task1 = new SendMsgTask(part1);
            SendMsgTask task2 = new SendMsgTask(part2);

            task1.fork();
            task2.fork();

            task1.join();
            task2.join();

        }else{
            for(String str : msgList){
                System.out.println(str.toString());
            }
        }

    }

    public static void main(String args[]) throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();
        List<String> msgList = new ArrayList<String>();
        for(int i =0 ; i<100 ; i++){
            msgList.add("send message"+i);
        }
        SendMsgTask task = new SendMsgTask(msgList);
        pool.submit(task);

    }
}
