package com.bugai.springframework.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 单行转对象， 多行转Map
 * @param <T>
 */
public interface RowMapper<T> {

  T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
