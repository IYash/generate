package com.person.connectionPool;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by huangchangling on 2018/5/18.
 * 测试连接池的使用情况
 * 模拟客户端ConnectionRunner获
 * 取、使用、最后释放连接的过程，当它使用时连接将会增加获取到连接的数量，反之，将会增
 * 加未获取到连接的数量
 */
public class ConnectionPoolTest {
    //获取到的连接数
    private static AtomicInteger got = new AtomicInteger();
    //未获取到的连接数
    private static AtomicInteger notGet = new AtomicInteger();

    //通过CountDownLatch模拟并发
    private static CountDownLatch start =new CountDownLatch(1);
    private static CountDownLatch end ;//等待任务执行结束

    public static void main(String[] args) throws Exception{
        ConnectionPool pool = new ConnectionPool(10);
        //模拟任务，获取线程
        int count = 10;//并发数
        end = new CountDownLatch(10);
        for(int i=0;i<count;i++){
            Thread t = new Thread(new ConnectionRunner(pool),"runner_"+i);
            t.start();
        }
        start.countDown();
        end.await();
        System.out.println("got connection times-----------------"+ got.get());
        System.out.println("notGet connection times-----------------"+ notGet.get());
    }
    static class ConnectionRunner implements Runnable {
        private int count = 100;//执行2次
        private ConnectionPool pool;

        public ConnectionRunner(ConnectionPool pool) {
            this.pool = pool;
        }

        @Override
        public void run() {
            try {
                start.await();//等待countdown完成
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Connection conn = null;//获取连接
            while (count > 0) {
                try {
                    conn = pool.fetchConnection(1000);
                    if (conn != null) {
                        got.getAndDecrement();
                        conn.createStatement();
                        conn.commit();//执行任务
                    } else {
                        notGet.getAndDecrement();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    pool.releaseConnection(conn);//释放连接
                    count--;
                }
            }
            end.countDown();//等待当前任务执行结束
        }
    }

}
