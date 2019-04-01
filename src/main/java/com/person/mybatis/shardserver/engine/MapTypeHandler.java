package com.person.mybatis.shardserver.engine;


import com.person.mybatis.shardserver.util.XmlUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class MapTypeHandler extends BaseTypeHandler<Map<String,Object>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Map<String, Object> stringObjectMap, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i,XmlUtil.toXml(stringObjectMap));
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return (Map<String,Object>)XmlUtil.fromXML(resultSet.getString(columnName));
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return (Map<String,Object>)XmlUtil.fromXML(resultSet.getString(columnIndex));
    }

    @Override
    public Map<String, Object> getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return (Map<String, Object>) XmlUtil.fromXML(callableStatement.getString(columnIndex));
    }
}
