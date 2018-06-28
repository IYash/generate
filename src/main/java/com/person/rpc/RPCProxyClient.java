package com.person.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by huangchangling on 2018/6/28.
 * 1.如何调用他人的远程服务
 * 1.1怎么做到透明化远程服务调用：使用代理，这里用jdk动态代理
 * 1.2怎么对消息进行编码和解码：
 * 1.2.1确定消息数据结构
 * 消费端：接口名称，方法名，参数类型&参数值，超时时间，requestId
 * 服务端：返回值，状态code,requestId
 * 1.2.2序列化：通用性，性能（时间复杂度和空间复杂度），可扩展性（protobuf结合此分析）
 * 1.3通信：使用java nio方式自研，基于mina，基于netty（主导）
 * 1.4消息里为什么要有requestId？（怎么让当前线程“暂停”，等结果回来后，再向后执行？怎么知道哪个消息结果是原先哪个线程调用的？）
 * 2.如何发布自己的服务
 */
public class RPCProxyClient<T> implements InvocationHandler {

    private T t;//通过泛型实现
    public RPCProxyClient(T t) {
        this.t = t;
    }

    /**
     * 调用此方法执行
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //结果参数
       Object result = new Object();
        //通信调用逻辑
        return result;
    }

    /**
     *  得到被代理对象
     * @param t
     * @param <T>
     * @return
     */
    public static <T>T getProxy(T t){
        return (T)Proxy.newProxyInstance(t.getClass().getClassLoader(),t.getClass().getInterfaces(),new RPCProxyClient(t));
    }
}
