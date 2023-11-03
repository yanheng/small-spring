package com.bugai.springframework.jdbc.suport;

import com.bugai.springframework.utils.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.*;

public class JdbcUtils {

  public static void closeStatement(Statement statement) {
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
      }
    }
  }

  public static void closeResultSet(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
      }
    }
  }

  public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
    String columnName = resultSetMetaData.getColumnLabel(columnIndex);
    if (StringUtils.isEmpty(columnName)) {
      columnName = resultSetMetaData.getColumnName(columnIndex);
    }
    return columnName;
  }

  public static Object getResultSetValue(ResultSet rs, int columnIndex) throws SQLException {
    Object object = rs.getObject(columnIndex);
    String className = null;
    if (object != null) {
      className = object.getClass().getName();
    }

    if (object instanceof Blob) {
      Blob blob = (Blob) object;
      object = blob.getBytes(1, (int) blob.length());
    } else if (object instanceof Clob) {
      Clob clob = (Clob) object;
      object = clob.getSubString(1, (int) clob.length());
    } else if ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className)) {
      object = rs.getTimestamp(columnIndex);
    } else if (null != className && className.startsWith("oracle.sql.DATE")) {
      String metadataClassName = rs.getMetaData().getColumnClassName(columnIndex);
      if ("java.sql.Timestamp".equals(metadataClassName) || "oracle.sql.TIMESTAMP".equals(metadataClassName)) {
        object = rs.getTimestamp(columnIndex);
      } else {
        object = rs.getDate(columnIndex);
      }
    } else if (object instanceof Date) {
      if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(columnIndex))) {
        object = rs.getDate(columnIndex);
      }
    }
    return object;

  }

  public static Object getResultSetValue(ResultSet rs, int index, Class<?> requiredType) throws SQLException {
    if (requiredType == null) {
      return getResultSetValue(rs, index);
    }

    Object value;
    if (String.class == requiredType) {
      return rs.getString(index);
    } else if (boolean.class == requiredType || Boolean.class == requiredType) {
      value = rs.getBoolean(index);
    } else if (byte.class == requiredType || Byte.class == requiredType) {
      value = rs.getByte(index);
    } else if (short.class == requiredType || Short.class == requiredType) {
      value = rs.getShort(index);
    } else if (int.class == requiredType || Integer.class == requiredType) {
      value = rs.getInt(index);
    } else if (long.class == requiredType || Long.class == requiredType) {
      value = rs.getLong(index);
    } else if (float.class == requiredType || Float.class == requiredType) {
      value = rs.getFloat(index);
    } else if (double.class == requiredType || Double.class == requiredType ||
            Number.class == requiredType) {
      value = rs.getDouble(index);
    } else if (BigDecimal.class == requiredType) {
      return rs.getBigDecimal(index);
    } else if (Date.class == requiredType) {
      return rs.getDate(index);
    } else if (Time.class == requiredType) {
      return rs.getTime(index);
    } else if (Timestamp.class == requiredType || java.util.Date.class == requiredType) {
      return rs.getTimestamp(index);
    } else if (byte[].class == requiredType) {
      return rs.getBytes(index);
    } else if (Blob.class == requiredType) {
      return rs.getBlob(index);
    } else if (Clob.class == requiredType) {
      return rs.getClob(index);
    } else if (requiredType.isEnum()) {
      Object obj = rs.getObject(index);
      if (obj instanceof String) {
        return obj;
      } else if (obj instanceof Number) {
        return NumberUtils.convertNumberToTargetClass(((Number) obj), Integer.class);
      } else {
        return rs.getString(index);
      }

    } else {
      return rs.getObject(index, requiredType);
    }

    return (rs.wasNull() ? null : value);
  }
}
