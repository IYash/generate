package com.person.concurrent.demo.carDist;

import java.util.HashSet;
import java.util.Set;

/**
 * @author huangchangling on 2018/2/3 0003
 */
public class RobotPool {
    //quietly prevents identical entries
    private Set<Robot> pool = new HashSet<>();
    public synchronized void add(Robot r){
        pool.add(r);
        notifyAll();
    }
    public synchronized void hire(Class<?> clz, Assembler assembler) throws InterruptedException {
        for(Robot r:pool)
            if(r.getClass().equals(clz)){
                pool.remove(r);
                r.assignAssembler(assembler);
                r.engage();//power it up to do the task
                return ;
            }
        wait();//none available
        hire(clz,assembler);//try again,recursively
    }

    public synchronized void release(Robot robot) {
        add(robot);
    }
}
