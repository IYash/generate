package com.person.script;

import javax.script.*;
import java.util.Date;

/**
 * Created by huangchangling on 2018/7/11.
 */
public class ScriptEngineExample {
    public static void main(String[] args) throws Exception {
        evalScript();
    }
    public static void evalScript() throws Exception {
        ScriptEngineManager factory = new ScriptEngineManager();
        //每次生成一个engine实例
        ScriptEngine engine = factory.getEngineByName("groovy");
        System.out.println(engine.toString());
        //javax.script.Bindings
        Bindings binding = engine.createBindings();
        binding.put("date",new Date());
        //如果script文本来自文件，请首先获取文件内容
        engine.eval("def getTime(){return date.getTime();}",binding);
        engine.eval("def sayHello(name,age){return 'hello i am '+name+',age '+ age;}");
        Long time = (Long)((Invocable)engine).invokeFunction("getTime",null);
        System.out.println(time);
        String message = (String)((Invocable)engine).invokeFunction("sayHello","zhangsan",12);
        System.out.println(message);

    }
}
