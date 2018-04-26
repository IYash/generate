package com.person.generate.dao;


import org.junit.Test;

import java.text.MessageFormat;

/**
 * @author huangchangling on 2018/1/10 0010
 */
public class MessageFormatTest {
    public static String pattern = "hello world {0} see you again";
    @Test
    public void testFormat(){
       String param = "json";
       String ret = MessageFormat.format(pattern,param);
       System.out.println(ret);
    }
}
