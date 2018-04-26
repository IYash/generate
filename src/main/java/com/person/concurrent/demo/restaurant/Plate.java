package com.person.concurrent.demo.restaurant;

/**
 * @author huangchangling on 2018/1/24 0024
 * this is what comes back from the chef
 */
public class Plate {
    private final Order order;
    private final Food food;

    public Plate(Order order, Food food) {
        this.order = order;
        this.food = food;
    }
    public Order getOrder(){return order;}
    public Food getFood(){return food;}
    public String toString(){return food.toString();}
}
