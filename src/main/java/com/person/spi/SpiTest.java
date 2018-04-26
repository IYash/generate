package com.person.spi;

import java.util.ServiceLoader;

/**
 * Created by huangchangling on 2018/4/24.
 */
public class SpiTest {
    public static void main(String[] args) {
        ServiceLoader<DatabaseInterface> loaders = ServiceLoader.load(DatabaseInterface.class);
        int i= 0;
        for(DatabaseInterface in:loaders){
            in.querySth();
            i ++;
        }
        System.out.println();
        System.out.println("找到服务实现类："+i);
    }
}
