package com.person.generate.dao;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author huangchangling on 2018/1/18 0018
 */
public class NumberTests{
    @Test
    public void test(){
        double price = 100.07;
        BigDecimal b1 = new BigDecimal(price);
        System.out.println(b1);
       Long tLong = (new BigDecimal(price)).multiply(new BigDecimal(100.0D)).longValue();
        System.out.println(tLong);
    }
    @Test
    public void testString2Num(){
        String numStr = "1234";
        int num = Integer.parseInt(numStr);
        System.out.println(numStr);
        //自定义实现
        System.out.println(string2Num(numStr,10));
    }

    /**
     *
     * @param numStr 传入的字符串
     * @param radix 进制
     * @return
     */
    private int string2Num(String numStr,int radix){
        int len = numStr.length();
        int i= 0;
        int result = 0;
        int val =0;
        while(i<len){
            val = (int)numStr.charAt(i) -(int)('0');
            result *=radix;
            result +=val;
            i++;
        }
        return result;
    }

}
