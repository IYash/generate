package com.person.concurrent.demo.carDist;

/**
 * @author huangchangling on 2018/2/3 0003
 */
public class Car {
    private final int id;
    private boolean engine = false, driveTrain =false, wheels= false;
    //Empty car object
    public Car(){id =-1;}
    public Car(int id){this.id = id;}
    public synchronized int getId(){return id;}
    public synchronized void addEngine(){ engine  =true;}
    public synchronized void addDriveTrain(){driveTrain = true;}
    public synchronized void addWheels(){wheels = true;}
    public synchronized String toString(){
        return "Car" + id + " [" + "engine:"+ engine +" driveTrain:"
                + driveTrain+ " wheels:" + wheels +" ]";
    }
}
