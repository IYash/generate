package com.person.mybatis.shardserver.util;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ReflectionUtil {

    public static Map<Class,Field[]> declaredFieldsCache = new ConcurrentHashMap<>();
    public static <T> T findTarget(Class<T> clazz,Object declared,String name){
        Field field = findField(declared.getClass(),name,null);
        field.setAccessible(true);
        return (T) getField(field,declared);
    }
    public static void setTarget(Object declared,Object value,String name){
        Field field = findField(declared.getClass(),name,null);
        field.setAccessible(true);
        setField(field,declared,value);
    }
    public static Field findField(Class clazz,String name,Class type){
        for(Class searchType = clazz;Object.class != searchType && searchType !=null;searchType = searchType.getSuperclass()){
            Field[] fields = getDeclaredFields(searchType);
            Field[] currFields = fields;
            int len = fields.length;
            for(int index =0;index<len;index++){
                Field field = currFields[index];
                if((name==null || name.equals(field.getName()))&&(type==null || type.equals(field.getType()))){
                    return field;
                }
            }
        }
        return null;
    }
    public static void setField(Field field,Object declared,Object value){
        try {
            field.set(declared,value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public static <T> T getField(Field field,Object declared){
        try {
            return (T)field.get(declared);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Field[] getDeclaredFields(Class<?> clazz){
        Field[] result = declaredFieldsCache.get(clazz);
        if(result == null){
            result = clazz.getDeclaredFields();
            declaredFieldsCache.put(clazz,result);
        }
        return result;
    }
}
