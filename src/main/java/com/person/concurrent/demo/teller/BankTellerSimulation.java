package com.person.concurrent.demo.teller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author huangchangling on 2018/1/22 0022
 */
public class BankTellerSimulation {
    static final int MAX_LINE_SIZE=5;
    static final int ADJUSTMENT_PERIOD=1000;

    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        //if line is too long,customers will leave
        CustomerLine customers = new CustomerLine(MAX_LINE_SIZE);
        exec.execute(new CustomerGenerator(customers));
        //manager will add and remove tellers as necessary
        exec.execute(new TellerManager(exec,ADJUSTMENT_PERIOD,customers));
        if(args.length>0)//Optional argument
            TimeUnit.SECONDS.sleep(new Integer(args[0]));
        else{
            System.out.println("Press 'Enter' to quit");
            System.in.read();
        }
        exec.shutdown();
    }
}
