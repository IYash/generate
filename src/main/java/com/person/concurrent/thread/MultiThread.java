package com.person.concurrent.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * Created by huangchangling on 2018/5/2.
 * java程序天生就是多线程程序，因为执行main()方法的是一个名称为main的线程
 * 设置线程优先级时，针对频繁阻塞（休眠或者I/O操作）的线程需要设置较高优先级，
 * 而偏重计算（需要较多CPU时间或者偏运算）的线程则设置较低的优先级，确保处理器不会被独占
 * 不要依赖对线程优先级的设定
 */
public class MultiThread {
    public static void main(String[] args) {
        //获取java线程管理MXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        //不需要获取同步的monitor和synchronizer信息，仅获取线程和线程栈信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false,false);
        //遍历线程信息，仅打印线程id和线程名称信息
        for(ThreadInfo threadInfo:threadInfos){
            System.out.println("["+threadInfo.getThreadId() +"] "+threadInfo.getThreadName());
        }
    }

}
