package com.person.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by huangchangling on 2018/4/20.
 */
public class DynamicProxyHandler implements InvocationHandler {

    private Object proxied;

    public DynamicProxyHandler(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("**** proxy: " + proxy.getClass() + ",method: "+method +",args:"+args);
        if(args != null)
            for (Object arg:args) {
                System.out.println(" " + arg);
            }
        return method.invoke(proxied,args);
    }
}
class SimpleDynamicProxy{
    public static void consumer(InterfaceD iface){
        iface.doSomething();
        iface.somethingElse("bonono");
    }

    public static void main(String[] args) {
        RealObject real = new RealObject();
        consumer(real);
        InterfaceD proxy = (InterfaceD) Proxy.newProxyInstance(InterfaceD.class.getClassLoader(),new Class[]{InterfaceD.class},new DynamicProxyHandler(real));
        consumer(proxy);
    }
}
