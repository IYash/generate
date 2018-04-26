package com.person.reflect;


/**
 * Created by huangchangling on 2018/4/20.
 */
public class RealObject implements InterfaceD {
    @Override
    public void doSomething() {
        System.out.println("doSomething");
    }

    @Override
    public void somethingElse(String arg) {
        System.out.println("somethingElse"+ arg);
    }
}
 class SimpleProxy implements InterfaceD{
     private InterfaceD proxied;

     public SimpleProxy(InterfaceD proxied) {
         this.proxied = proxied;
     }

     @Override
     public void doSomething() {
         System.out.println("SimpleProxy doSomething");
         proxied.doSomething();
     }

     @Override
     public void somethingElse(String arg) {
         System.out.println("SimpleProxy somethingElse"+ arg);
         proxied.somethingElse(arg);
     }
 }
class SimpleProxyDemo{
    public static void consumer(InterfaceD iface){
        iface.doSomething();
        iface.somethingElse("bonobo");
    }

    public static void main(String[] args) {
        consumer(new RealObject());
        consumer(new SimpleProxy(new RealObject()));
    }
}
