package com.person.generic;

/**
 * Created by huangchangling on 2018/9/21.
 * 泛型操作练习
 * 抽象植物工厂
 * 使用泛型限界符实现，T is-a Plant
 */
public abstract class AbstractPlant<T extends Plant> {
    public abstract  void method(T t);
}
