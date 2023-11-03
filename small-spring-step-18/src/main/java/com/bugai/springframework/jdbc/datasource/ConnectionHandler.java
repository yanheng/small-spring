package com.bugai.springframework.jdbc.datasource;

import java.sql.Connection;

public interface ConnectionHandler {
  Connection getConnection();

  default void releaseConnection(Connection con) {

  }
}
