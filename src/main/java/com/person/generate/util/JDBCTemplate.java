package com.person.generate.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author huangchangling on 2017/10/16 0016
 */
public class JDBCTemplate {

    //插入、修改、删除模板
    public static void update(String sql,Object... parameters){
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = JDBCUtil.INSTANCE.getConn();
            statement = conn.prepareStatement(sql);
            initParams(statement, parameters);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.INSTANCE.close(conn,statement,null);
        }


    }
    //查询模板
    public static <T>List<T> query(String sql,IRSHandler<T> rsh,Object... parameters){
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.INSTANCE.getConn();
            statement = conn.prepareStatement(sql);
            initParams(statement, parameters);
            rs= statement.executeQuery();
            return rsh.handle(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.INSTANCE.close(conn,statement,rs);
        }
        return null;
    }

    private static void initParams(PreparedStatement statement, Object[] parameters) throws SQLException {
        for(int i=0;i<parameters.length;i++){
            statement.setObject(i+1,parameters[i]);
        }
    }
}
