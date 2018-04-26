package com.person.concurrent.demo.restaurant;

/**
 * @author huangchangling on 2018/1/24 0024
 * this is given to the waiter, who gives it the chef
 */
public class Order {//A DATA-TRANSFER OBJECT
    private static int counter = 0;
    private final int id  = counter++;
    private final Customer customer;
    private final WaitPerson waitPerson;
    private final Food food;

    public Order(Customer customer, WaitPerson waitPerson, Food food) {
        this.customer = customer;
        this.waitPerson = waitPerson;
        this.food = food;
    }
    public Food item(){ return food;}
    public Customer getCustomer(){return customer;}
    public WaitPerson getWaitPerson(){return waitPerson;}
    public String toString(){
        return "Order:"+id + " item:"+food+" for:"+customer+" served by:"+waitPerson;
    }
}
