package com.person.generate.generic;

import com.person.generic.AbstractPlant;
import com.person.generic.Flower;
import com.person.generic.FlowerPlant;

import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by huangchangling on 2018/9/21.
 */
public class GenericTest {

    private AbstractPlant<Flower> fp = new FlowerPlant();
    /**
     * 获取父类的类型，泛型类型，泛型参数
     */
    @Test
    public void testGetSuperGeneric(){
        Class clz = fp.getClass();
        Type[] iTypes = clz.getGenericInterfaces();
        for(Type i : iTypes) System.out.println(i);
        Type type = clz.getSuperclass();
        System.out.println(type);
        Type genType = clz.getGenericSuperclass();
        System.out.println(genType);
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        for(Type i:params) System.out.println(i);
    }

}
