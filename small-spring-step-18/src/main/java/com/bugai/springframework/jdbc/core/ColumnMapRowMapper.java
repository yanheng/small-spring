package com.bugai.springframework.jdbc.core;

import com.bugai.springframework.jdbc.suport.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ColumnMapRowMapper implements RowMapper<Map<String, Object>> {

  @Override
  public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
    ResultSetMetaData metaData = rs.getMetaData();
    int columnCount = metaData.getColumnCount();
    Map<String, Object> columnMap = createColumnMap(columnCount);
    for (int i = 1; i <= columnCount; i++) {
      String columnName = JdbcUtils.lookupColumnName(metaData, i);
      Object resultSetValue = JdbcUtils.getResultSetValue(rs, i);
      columnMap.putIfAbsent(columnName, resultSetValue);
    }
    return columnMap;
  }

  private Map<String, Object> createColumnMap(int columnCount) {
    return new LinkedHashMap<>(columnCount);
  }
}
