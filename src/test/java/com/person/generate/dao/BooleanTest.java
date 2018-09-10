package com.person.generate.dao;

import org.junit.Test;

/**
 * Created by huangchangling on 2018/9/3.
 */
public class BooleanTest {

    private static final String A="A";
    private static final String B="B";
    @Test
    public void test(){
        String c = "A";
        //当c为A或B时返回,逆否命题是c不为A且c不为B
        if ( c == A || c== B) return ;
        if (c != A) System.out.println("1234===");
        if (c != B) System.out.println("2345====");
    }

    @Test
    public void testZero(){
        boolean a = true;
        try{
            a = false;
            return;
        }finally{
            if(a) System.out.println("1234=========");
        }

    }

}
