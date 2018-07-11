package com.person.script;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;

import java.io.File;
import java.util.Date;

/**
 * Created by huangchangling on 2018/7/11.
 * 解析groovy文件
 */
public class GroovyClassLoaderExample {

    public static void main(String[] args) throws Exception{
        parse();
    }

    public static void parse() throws Exception {
        GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
        File file = new File("D:\\workspace4idea\\svn\\other\\generate\\src\\main\\java\\com\\person\\script\\TestGroovy.groovy");
        Class testGroovyClass = classLoader.parseClass(new GroovyCodeSource(file));
        GroovyObject instance = (GroovyObject)testGroovyClass.newInstance();
        Long time = (Long) instance.invokeMethod("getTime",new Date());
        System.out.println(time);
        Date date  = (Date)instance.invokeMethod("getDate",time);
        System.out.println(date.getTime());
        instance = null;
        testGroovyClass = null;
    }
}
