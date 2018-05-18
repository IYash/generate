package com.person.connectionPool;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * Created by huangchangling on 2018/5/18.
 * 需求描述：
 * 我们使用等待超时模式来构造一个简单的数据库连接池，在示例中模拟从连接池中获
 * 取、使用和释放连接的过程，而客户端获取连接的过程被设定为等待超时的模式，也就是在
 * 1000毫秒内如果无法获取到可用连接，将会返回给客户端一个null。设定连接池的大小为10
 * 个，然后通过调节客户端的线程数来模拟无法获取连接的场景。
 */
public class ConnectionPool {
    //连接存放容器
    private LinkedList<Connection> pool = new LinkedList<>();
    //初始化对象时，生成Connection,默认初始化size个连接
    public ConnectionPool(int size){
        if(pool.size()<size){
            pool.addLast(ConnectionDriver.createConnection());
        }
    }

    //获取连接,等待超时
    public Connection fetchConnection(long mills) throws Exception{
        Connection conn = null;
        //安全的获取连接
        synchronized (pool){
            if(!pool.isEmpty()){
                conn= pool.removeFirst();
                return conn;
            }
            long remaining = mills + System.currentTimeMillis();
            while ( pool.isEmpty() && remaining>0){
                pool.wait(mills);//等待mills
                remaining -= System.currentTimeMillis();//意外唤醒的补偿处理
            }
            if(!pool.isEmpty())
            conn =pool.removeFirst();
            return conn;
        }
    }

    //释放连接
    public void releaseConnection(Connection conn){
            if(conn != null) {
                synchronized (pool) {
                    pool.addLast(conn);
                    //唤醒可能处于等待获取连接的线程
                    pool.notify();//notifyAll的效率比较低
                }
            }
    }
}
