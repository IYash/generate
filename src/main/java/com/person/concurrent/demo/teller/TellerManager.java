package com.person.concurrent.demo.teller;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author huangchangling on 2018/1/18 0018
 */
public class TellerManager implements Runnable {
    private ExecutorService  exec;
    private CustomerLine customers;
    private PriorityQueue<Teller> workingTellers = new PriorityQueue();
    private Queue<Teller> tellersDoingOtherThings = new LinkedList<>();
    private int adjustmentPeriod;
    private static Random rand = new Random(47);

    public TellerManager(ExecutorService exec, int adjustmentPeriod, CustomerLine customers) {
        this.exec = exec;
        this.adjustmentPeriod = adjustmentPeriod;
        this.customers = customers;
        //start with a single teller
        Teller teller = new Teller(customers);
        exec.execute(teller);
        workingTellers.add(teller);
    }
    public void adjustTellerNumber(){
        //this is actually a control system.By adjusting
        //the numbers.you can reveal stability issues in the
        //control mechanism.
        //if line is too long,add another teller:
        if(customers.size() / workingTellers.size() >2){
            //if tellers are on break or doing another job,bring one back:
            if(tellersDoingOtherThings.size() > 0){
                Teller teller = tellersDoingOtherThings.remove();
                teller.serveCustomerLine();
                workingTellers.offer(teller);
                return;
            }
            //else create (hire)a new teller
            Teller teller = new Teller(customers);
            exec.execute(teller);
            workingTellers.add(teller);
            return;
        }
        //if line is short enough,remove a teller
        if(workingTellers.size()>1 && customers.size()/workingTellers.size()<2)
            reassignOneTeller();
        // if there is no line,we only need one teller
        if(customers.size() == 0){
            while(workingTellers.size() >1) reassignOneTeller();
        }
    }
    //give a teller a different job or a break
    private void reassignOneTeller() {
        Teller teller = workingTellers.poll();
        teller.doSomethingElse();
        tellersDoingOtherThings.offer(teller);
    }

    @Override
    public void run() {
        try {
        while(!Thread.interrupted()){

                TimeUnit.MILLISECONDS.sleep(adjustmentPeriod);
                adjustTellerNumber();
            System.out.println(customers + " { ");
            for (Teller teller:workingTellers)
                System.out.println(teller.shortString()+" ");
            System.out.println(" }");
        }
        } catch (InterruptedException e) {
            System.out.println(this + "interrupted");
            }
        System.out.println(this+" terminating");

    }
    public String toString(){
        return "TellerManager ";
    }
}
