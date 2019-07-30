package com.Dream.typeHandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.apache.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@MappedJdbcTypes(value = JdbcType.DATE, includeNullJdbcType = true)
@MappedTypes(value = LocalTime.class)
public class LocalDateTypeHandler implements TypeHandler<LocalDate> {

    Logger logger = Logger.getLogger(LocalDateTypeHandler.class);

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, LocalDate localDate, JdbcType jdbcType) throws SQLException {
        logger.info("localDateTypeHandler 设置参数" + " : localDate = " + localDate );
        preparedStatement.setString(i,localDate.toString());
    }

    @Override
    public LocalDate getResult(ResultSet resultSet, String s) throws SQLException {
        logger.info("localDateTypeHandler 获取结果:  column name = " + s);
        String result = resultSet.getString(s);
        LocalDate localDate = LocalDate.parse(result, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        logger.info("解析出来对象:" + localDate);
        return localDate;
    }

    @Override
    public LocalDate getResult(ResultSet resultSet, int i) throws SQLException {
        logger.info("localDateTypeHandler 获取结果:  column index = " + i);
        String result = resultSet.getString(i);
        LocalDate localDate = LocalDate.parse(result, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        logger.info("解析出来对象:" + localDate);
        return localDate;
    }

    @Override
    public LocalDate getResult(CallableStatement callableStatement, int i) throws SQLException {
        logger.info("localDateTypeHandler 获取结果:  column index = " + i);
        String result = callableStatement.getString(i);
        LocalDate localDate = LocalDate.parse(result, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        logger.info("解析出来对象:" + localDate);
        return localDate;
    }
}
