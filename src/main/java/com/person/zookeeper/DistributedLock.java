package com.person.zookeeper;







import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by huangchangling on 2018/4/26.
 * zookeeper的watcher机制主要包括客户端线程，客户端watchermanager和zookeeper服务器三部分
 * 具体工作流程为：
 * 客户端在向zookeeper服务器注册watcher的同时，会将watcher对象存储在客户端的watchermanager中，
 * 当服务端触发watcher事件后，会向客户端发送通知，客户端线程从watchermanager中取出对应的watcher
 * 对象，根据通知类型和节点路径，来处理回调逻辑，对客户端数据做出相应处理
 * watch工作机制：客户端注册watcher，服务端处理watcher和客户端回调watcher事件
 */
public class DistributedLock implements Lock,Watcher {
    private ZooKeeper zk = null;
    //根节点
    private String ROOT_LOCK = "/locks";
    //竞争的资源
    private String lockName;
    //等待的前一个锁
    private String WAIT_LOCK;
    //当前锁
    private String CURRENT_LOCK;
    //计数器
    private CountDownLatch countDownLatch;
    private int sessionTimeout = 30000;
    private List<Exception> exceptionList = new ArrayList<>();

    /**
     * 配置分布式锁
     * @param config 连接url
     * @param lockName 竞争资源
     */
    public DistributedLock(String config,String lockName){
        this.lockName = lockName;
        try {
            //在通过客户端接口调用时，可以指定一个watcher接口，以接受服务器时间的回调
            zk = new ZooKeeper(config,sessionTimeout,this);
            Stat stat = zk.exists(ROOT_LOCK,false);
            if (stat == null){
                //如果根节点不存在，则创建根节点
                zk.create(ROOT_LOCK,new byte[0],ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void lock() {
        if(exceptionList.size()>0)
            throw new LockException(exceptionList.get(0));
        if(this.tryLock()){
            System.out.println(Thread.currentThread().getName()+" "+lockName+" 获得了锁");
            return ;
        }else{
            //等待锁
            try {
                waitForLock(WAIT_LOCK,sessionTimeout);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean waitForLock(String wait_lock, long sessionTimeout) throws KeeperException, InterruptedException {
        Stat stat = zk.exists(ROOT_LOCK+"/"+wait_lock,true);//这个路径下发生create/delete事件时，监听事件将被触发
        if(stat !=null){
            System.out.println(Thread.currentThread().getName()+"等待锁 "+ ROOT_LOCK+"/"+wait_lock);
            this.countDownLatch = new CountDownLatch(1);
            //计数等待，若等到前一个节点消失。则process中进行countdown,停止等待，获取锁
            this.countDownLatch.await(sessionTimeout,TimeUnit.MILLISECONDS);
            this.countDownLatch = null;
            System.out.println(Thread.currentThread().getName()+" 等到了锁");
        }
        return true;
    }
    @Override
    public void lockInterruptibly() throws InterruptedException {
            this.lock();
    }

    @Override
    public boolean tryLock() {
        String splitStr="_lock_";
        if(lockName.contains(splitStr)){
            throw new LockException("锁名有误");
        }
        //创建临时有序节点
        try {
            CURRENT_LOCK = zk.create(ROOT_LOCK + "/"+lockName + splitStr,new byte[0],
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(CURRENT_LOCK + " 已经创建");
            //取所有子节点
            List<String> subNodes = zk.getChildren(ROOT_LOCK,false);
            //取出所有lockName的锁
            List<String> lockObjects = new ArrayList<>();
            for (String node:subNodes) {
                String _node = node.split(splitStr)[0];
                if(_node.equals(lockName)) lockObjects.add(node);
            }
            Collections.sort(lockObjects);
            System.out.println(Thread.currentThread().getName()+" 的锁是 "+CURRENT_LOCK);
            //若当前节点为最小节点，则获取锁成功
            if(CURRENT_LOCK.equals(ROOT_LOCK+"/"+lockObjects.get(0))) return true;
            //若不是最小节点,则找到自己的前一个节点
            String prevNode = CURRENT_LOCK.substring(CURRENT_LOCK.lastIndexOf("/")+1);
            WAIT_LOCK = lockObjects.get(Collections.binarySearch(lockObjects,prevNode)-1);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        if(this.tryLock()) return true;
        try {
            return waitForLock(WAIT_LOCK,time);
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void unlock() {
        System.out.println("释放锁 "+ CURRENT_LOCK);
        try {
            zk.delete(CURRENT_LOCK,-1);
            CURRENT_LOCK = null;
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
    //节点监视器
    @Override
    public void process(WatchedEvent watchedEvent) {
        //事件注册是一次性的，因为在每次处理事件之后，就会将相应的watcher注册删除
        //客户端接收到的服务端watcher事件中并不包含事件更改的具体内容，只是告知发生了这样一个watcher事件
        //所以客户端在接收到watcher事件之后，需要在回调函数process中对服务端的数据进行进行重新获取才能获得更改的具体内容
        if(this.countDownLatch !=null)
            this.countDownLatch.countDown();
    }

    private class LockException extends RuntimeException {
        public LockException(String e){
            super(e);
        }
        public LockException(Exception e){
            super(e);
        }
    }

}
