package com.bugai.springframework.jdbc.core;

import com.bugai.springframework.jdbc.IncorrectResultSetColumnCountException;
import com.bugai.springframework.jdbc.suport.JdbcUtils;
import com.bugai.springframework.utils.NumberUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SingleColumnRowMapper<T> implements RowMapper<T> {

  private Class<?> requiredType;

  public SingleColumnRowMapper(Class<?> requiredType) {
    this.requiredType = requiredType;
  }

  @Override
  public T mapRow(ResultSet rs, int rowNum) throws SQLException {
    ResultSetMetaData metaData = rs.getMetaData();
    int columnCount = metaData.getColumnCount();
    if (columnCount != 1) {
      throw new IncorrectResultSetColumnCountException(1, columnCount);
    }
    Object resultSetValue = JdbcUtils.getResultSetValue(rs, 1, requiredType);
    if (resultSetValue != null && resultSetValue != null && requiredType.isInstance(resultSetValue)) {
      return (T) convertValueToRequiredType(resultSetValue, requiredType);
    }
    return (T) resultSetValue;
  }

  protected Object convertValueToRequiredType(Object value, Class<?> requiredType) {
    if (String.class == requiredType) {
      return value.toString();
    } else if (Number.class.isAssignableFrom(requiredType)) {
      return NumberUtils.convertNumberToTargetClass((Number) value, (Class<Number>) requiredType);
    } else {
      throw new IllegalArgumentException(
              "Value [" + value + "] is of type [" + value.getClass().getName() +
                      "] and cannot be converted to required type [" + requiredType.getName() + "]");
    }
  }


}
