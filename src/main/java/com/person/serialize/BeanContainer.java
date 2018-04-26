package com.person.serialize;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangchangling on 2018/1/4 0004
 * 作为一个容器使用
 */
public class BeanContainer {
    /**
     * 单例的实现过程，有多种
     */
    private static volatile BeanContainer instance;//返回对象

    private BeanContainer(){}

    public static BeanContainer getInstance(){
        if(instance == null){
            synchronized (BeanContainer.class){
                if(instance == null)
                instance = new BeanContainer();
            }
        }
        return instance;
    }
    private ConcurrentHashMap<String,Object> container = new ConcurrentHashMap<>();

    public <T>T getBeanByType(Class<T> type){
        return getBeanByType(type,true);
    }

    public <T>T getBeanByType(Class<T> type,boolean isSingleTon){
        T result = null;
        if(isSingleTon){
            Collection values = container.values();
            for(Object value:values){
                if(value.getClass()== type) {
                    result = (T)value; return result;
                }
            }
            String simpleName = type.getSimpleName();
            String beanName = lowerFirstChar(simpleName);
            try {
                result = type.newInstance();
                container.put(beanName,result);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }else{
            try {
                result = type.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    private String lowerFirstChar(String source){
        char[] cs = null;
        if(source!=null && source!=""){
            cs = source.toCharArray();
            cs[0] = Character.toLowerCase(cs[0]);
        }
        return new String(cs);
    }

}
