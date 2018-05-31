package com.person.limit;

import com.netflix.hystrix.HystrixCommand;

import java.util.concurrent.TimeUnit;

/**
 * Created by huangchangling on 2018/5/24.
 */
public class CommandHelloWorld4 extends HystrixCommand<String> {

    private Integer id;

    public CommandHelloWorld4(Integer id) {
        super(setter());
        this.id = id;
    }

    private static Setter setter() {
        return ApiSetter.setter("getNum");
    }

    @Override
    protected String run() throws Exception {
        TimeUnit.MINUTES.sleep(3);
        System.out.println(Thread.currentThread().getName() + " execute id = " + id);
        return "run execute=" + id;
    }

    @Override
    protected String getFallback() {
        return "getFallback --" + id;
    }

    public static void main(String[] args) {
        CommandHelloWorld4 commandHelloWorld4_a = new CommandHelloWorld4(2);
        System.out.println("a执行结果:" + commandHelloWorld4_a.execute());
    }
}

