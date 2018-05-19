package com.person.threadPool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by huangchangling on 2018/5/19.
 */
public class CustomizeThreadPool<Job extends Runnable> implements ThreadPool<Job> {

    //任务容器,假设容器大小设置为100
    private LinkedList<Job> jobs = new LinkedList<>() ;
    //worker队列,可以配置核心线程数和最大线程数
    private List<Runnable> workers = new ArrayList<>();
    private int coreSize = 3;
    private int maxSize = 10 ;
    private int jobSize = 20;
    //线程命名前缀
    private static final String FIX="Thread-pool-";
    public CustomizeThreadPool() {
    }

    public CustomizeThreadPool(int coreSize, int maxSize,int jobSize) {
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.jobSize = jobSize;
        addWorkers(1);//预热核心个线程数
    }

    //锁对象
    private Object lock = new Object();
    @Override
    public void execute(Job job) {
        if(job != null) {
            synchronized (lock){
                if(workers.size()<coreSize)
                    addWorker(job);
                else if (jobs.size()<jobSize){
                    jobs.addLast(job);
                    lock.notify();
                }
                else if (workers.size()<maxSize){
                    addWorker(job);
                }else {
                    throw new RuntimeException("任务添加失败");
                }

            }
        }
    }

    @Override
    public void shutdown() {
    }
    //假设只被构造器调用
    @Override
    public void addWorkers(int num) {
        //当工作线程数小于maxSize时允许添加
        int currentSize = workers.size();
        int cSize = currentSize;//动态变化
        while(num > 0 && cSize < maxSize){
            addWorker(null);
            num -- ;
            cSize ++;
        }
    }
    private void addWorker(Job job){
        synchronized (lock){
            int currentSize = workers.size();
            Worker worker = new Worker(null);
            workers.add(worker);//用于记录工作中的worker
            Thread t = new Thread(worker ,FIX+(1+currentSize));
            t.start();//启动任务
        }

    }
    @Override
    public void removeWorker(int num) {

    }

    @Override
    public int getJobSize() {
        synchronized (lock){
            return jobs.size();
        }
    }
    //新建一个Worker可以执行指定的任务
    class Worker implements Runnable{
        Runnable firstJob;

        public Worker(Runnable firstJob) {
            this.firstJob = firstJob;
        }

        @Override
        public void run() {
            while(!Thread.interrupted()){
                if(firstJob != null){
                    firstJob.run();
                    firstJob = null;
                }else{
                    synchronized (lock) {
                        if (!jobs.isEmpty()) {
                            Job job = null;
                            if (!jobs.isEmpty())//这里的double check是没有问题的
                                job = jobs.removeFirst();
                            if (job != null) job.run();//执行任务
                        } else {//避免线程空转
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
