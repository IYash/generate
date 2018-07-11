package com.person.script;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.Date;

/**
 * Created by huangchangling on 2018/7/11.
 * 动态脚本语言的使用
 * 使用GroovyShell计算表达式
 */
public class GroovyShellExample {
    public static void main(String[] args) {
        //evalScriptText();
        evalScriptTextFull();
    }

    /**
     * GroovyShell通常用来运行‘script片段’或者一些零散的表达式
     */
    public static void evalScriptText(){
        //groovy.lang.binding
        Binding binding  = new Binding();
        GroovyShell shell = new GroovyShell(binding);

        binding.setVariable("name","zhangsan");
        shell.evaluate("println 'Hello world!I am ' + name;");
        //在script中，声明变量，不能使用def,否则scrope不一致
        shell.evaluate("date = new Date()");
        Date date = (Date)binding.getVariable("date");
        System.out.println("Date:"+date.getTime());
        //以返回值的方式，获取script内部变量值，或者执行结果
        //一个shell实例中，所有变量值，将会在此‘session’中传递下去.date可以在此后的script中获取
        Long time = (Long) shell.evaluate("def time = date.getTime();return time;");
        System.out.println("Time:"+time);
        binding.setVariable("list",new String[]{"A","B","C"});
        //invoke method
        String joinStr = (String)shell.evaluate("def call(){return list.join(' - ')};call()");
        System.out.println("Array join:"+joinStr);
        shell = null;
        binding = null;

    }
    /**
     * 运行完整脚本
     */
    public static void evalScriptTextFull(){
        StringBuilder sb = new StringBuilder();
        //define api
        sb.append("class User{")
                .append("String name;Integer age;")
                .append("String sayHello(){return 'Hello,I am '+name+' ,age '+age;}")
                .append("}\n");
        //Usage
        sb.append("def user = new User(name:'zhangsan',age:1);")
                .append("user.sayHello()");
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);
        String message = (String) shell.evaluate(sb.toString());
        System.out.println(message);
        shell = null;
    }
}
