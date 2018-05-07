package com.person.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by huangchangling on 2018/5/7.
 * 关于Retention的几点描述：
 * RESOURCE:在编译阶段丢失。这些朱姐在编译结束之后就不再有任何意义，所以不会写入字节码
 * CLASS:在类加载的时候丢弃。在字节码文件的处理中有用。注解默认使用这种方式
 * RUNTIME:始终不会丢弃，运行期页保留该注解，因此可以使用反射机制读取该注解的信息。
 * 我们自定义的注解通常使用这种方式
 * 注解本质是一个继承了Annotation的特殊接口，其具体实现类是JAVA运行时生成的动态代理类。
 * 而我们通过反射获取注解时，返回的是Java运行时生成的动态代理对象$Proxy。通过代理对象
 * 调用自定义注解（接口）的方法，会最终调用AnnotationInvocationHandler的invoke方法。该方法
 * 会从memberValues这个Map中索引出对应的值。而memberValues的来源是Java常量池
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomAnno {
    String value() default "";
}
