package com.person.concurrent.demo.carDist;

/**
 * @author huangchangling on 2018/2/3 0003
 */
public class WheelRobot extends Robot{

    public WheelRobot(RobotPool pool){super(pool);}

    @Override
    protected void performService() {
        System.out.println(this + " installing wheels");
        assembler.car().addWheels();
    }
}
