package com.person.generate.dao;

import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by huangchangling on 2018/4/16.
 */
public class PriorityTest {
    private BlockingQueue<ElementEntry> eeq = new PriorityBlockingQueue<>();
    static class ElementEntry implements Comparable{
        private int priority;

        public ElementEntry(int priority) {
            this.priority = priority;
        }
        @Override
        public int compareTo(Object o) {
            ElementEntry e = (ElementEntry) o;
            return priority > e.getPriority()?1:(priority == e.getPriority()?0:-1);
        }
        public int getPriority() {
            return priority;
        }
    }
    @Before
    public void testSiftUp(){
        ElementEntry e1 = new ElementEntry(5);
        ElementEntry e2 = new ElementEntry(4);
        ElementEntry e3 = new ElementEntry(6);
        ElementEntry e4 = new ElementEntry(7);
        ElementEntry e5 = new ElementEntry(3);
        ElementEntry e6 = new ElementEntry(2);
        eeq.offer(e1);
        eeq.offer(e2);
        eeq.offer(e3);
        eeq.offer(e4);
        eeq.offer(e5);
        eeq.offer(e6);
    }
    @Test public void testSiftDown(){
        do{
            System.out.println(eeq.poll().getPriority());
        }while(eeq.size()>0);
    }
}
