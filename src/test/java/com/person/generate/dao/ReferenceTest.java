package com.person.generate.dao;

import org.junit.Before;
import org.junit.Test;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangchangling on 2018/7/17.
 * -Xmx2m -XX:+PrintGCDetails
 */
public class ReferenceTest {

    private List<People> ps = new ArrayList<>();
    private List<WeakReference<People>> ws = new ArrayList<>();
    private int size = 1024*1024;
    private ThreadLocal<People> tp = new ThreadLocal<>();
    @Before
    public void initPs() throws InterruptedException {
        People p = new People();
        System.out.println(p);
        p.name = "name";
        ps.add(p);
        WeakReference sp = new WeakReference<>(p);
        ws.add(sp);
        tp.set(p);
        p.name = "change";
        p= null;
        byte[] bs = new byte[size];
        byte[] cs = new byte[size];
    }
    @Test
    public void testReference(){
        System.out.println(ps.size());
        System.out.println(ps.get(0)+"------->"+ps.get(0).name);
        System.out.println(ws.size());
        System.out.println(ws.get(0)+"------->"+ws.get(0).get().name);
        System.out.println(tp.get()+"------------");
    }
    class People{
        String name;
        @Override
        public void finalize(){
            System.out.println("===========finalize=========");
        }
    }
}
