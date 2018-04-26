package com.person.concurrent.demo.restaurant;

/**
 * @author huangchangling on 2018/1/24 0024
 */
public class Food {
    private static int counter = 0;
    private final int id = counter++;
    private final String name;
    public Food(String name){
        this.name =name;
    }
    public String toString(){
        return " " +name + " ";
    }
}
