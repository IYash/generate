package com.person.generate.util;

import java.sql.*;
import java.util.Properties;

/**
 * @author huangchangling on 2017/10/16 0016
 */
public enum JDBCUtil {

    INSTANCE;
    private static final String DB_SOURCE = "druid.properties";

    private static final Properties p = new Properties();
    //初始化资源
    static{

        try {
            p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(DB_SOURCE));
            Class.forName(p.getProperty("db.driver"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //获取连接
    public  Connection getConn() throws SQLException{

        return  DriverManager.getConnection(p.getProperty("db.url"),p.getProperty("db.username"),p.getProperty("db.password"));


    }
    //释放资源
    public  void close(Connection conn, Statement st, ResultSet rs){
        if(rs!=null)
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        if(st!=null)
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if(conn!=null)
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

}
