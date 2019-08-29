package com.Dream.commons.typeHandler;

import com.Dream.Enum.OauthTypeEnum;
import com.Dream.entity.Oauth;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(OauthTypeEnum.class)
@MappedJdbcTypes(JdbcType.INTEGER)
public class OauthTypeHandler implements TypeHandler<OauthTypeEnum> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, OauthTypeEnum oauthTypeEnum, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, oauthTypeEnum.getId());
    }

    @Override
    public OauthTypeEnum getResult(ResultSet resultSet, String s) throws SQLException {
        int id = resultSet.getInt(s);
        return OauthTypeEnum.getByID(id);
    }

    @Override
    public OauthTypeEnum getResult(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt(i);
        return OauthTypeEnum.getByID(id);
    }

    @Override
    public OauthTypeEnum getResult(CallableStatement callableStatement, int i) throws SQLException {
        int id = callableStatement.getInt(i);
        return OauthTypeEnum.getByID(id);
    }
}
