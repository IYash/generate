package com.person.concurrent.demo.carDist;

/**
 * @author huangchangling on 2018/2/3 0003
 */
public class DriveTrainRobot extends Robot{

    public DriveTrainRobot(RobotPool pool) {super(pool);}
    @Override
    protected void performService() {
        System.out.println(this + " installing driveTrain");
        assembler.car().addDriveTrain();
    }
}
