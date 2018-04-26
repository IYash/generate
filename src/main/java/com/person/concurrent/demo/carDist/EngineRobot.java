package com.person.concurrent.demo.carDist;

/**
 * @author huangchangling on 2018/2/3 0003
 */
public class EngineRobot extends Robot{

    public EngineRobot (RobotPool pool){super(pool);}
    @Override
    protected void performService() {
        System.out.println(this + " installing engine");
        assembler.car().addEngine();
    }
}
