package com.person.concurrent.demo.restaurant;

import java.util.Random;

/**
 * @author huangchangling on 2018/1/24 0024
 */
public enum Course {
    MEAT,
    RICE,
    VEGETABLE
    ;
    private Random rand = new Random(47);
    public Food randomSelection() {
        int index = rand.nextInt(3);
        return new Food(Course.values()[index].name());
    }
}
