package com.person.jmx;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by huangchangling on 2018/10/16.
 * jmxServiceUrl分析service:jmx:rmi://localhost:0/jndi/rmi://localhost:1099/jmxrmi
 * service:jmx 这个是JMX URL的标准前缀，以此开头否则MalformedURLException
 * rmi 这个是jmx connector server的传输歇息，这个url中使用rmi来进行传输的
 * localhost:0 这个是jmx connector server 的ip和端口，也就是真正提供服务端host和端口，忽略则随机绑定
 * jndi/rmi://localhost:1099/jmxrmi 这个是jmx connector server的路径，具体含义取决于前面的传输协议
 * 比如该URL中这串字符串就代表这该jmx connector server的stub是使用jndi api绑定在
 * rmi://localhost:1099/jmxrmi这个地址
 */
public class HelloAgent {

    public static void main(String[] args) throws Exception {
        //create mbean server
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        // create object name
        ObjectName objectName = new ObjectName("jmxBean:name=hello");
        //create mbean and register mbean
        server.registerMBean(new Hello(),objectName);
        /**
         * JMXConnectorServer service
         */
        //这句户非常重要，不能缺少！注册一个端口，绑定url后，客户端就可以使用
        //rmi通过url方式来连接JMXConnectorServer
        Registry registry = LocateRegistry.createRegistry(1099);
        //构造JMXServiceURL
        JMXServiceURL jmxServiceURL = new JMXServiceURL("" +
                "service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi");
        //创建JMXConnectorServer
        JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL,null,server);
        //启动
        cs.start();
    }

}
