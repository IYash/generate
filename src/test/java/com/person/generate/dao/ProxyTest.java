package com.person.generate.dao;

import com.person.generate.dao.impl.DefaultBlogDaoImpl;
import com.person.generate.proxy.DefaultInvocationHandler;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author huangchangling on 2017/12/22 0022
 */
public class ProxyTest {

    private IBlogDao blogDao = new DefaultBlogDaoImpl();

    @Test
    public void testProxy() throws Exception{

        InvocationHandler handler = new DefaultInvocationHandler(blogDao);

        ClassLoader loader = blogDao.getClass().getClassLoader();
        Class[] interfaces = blogDao.getClass().getInterfaces();
        IBlogDao subject = (IBlogDao) Proxy.newProxyInstance(loader, interfaces, handler);

        System.out.println("动态代理对象的类型："+subject.getClass().getName());
        //subject.selectById(33);
        subject.getClass().getMethod("selectById",int.class).invoke(subject,33);

    }

}
