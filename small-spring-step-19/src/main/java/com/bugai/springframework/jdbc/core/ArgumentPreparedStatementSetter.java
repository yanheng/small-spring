package com.bugai.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArgumentPreparedStatementSetter implements PreparedStatementSetter {

  private final Object[] args;

  public ArgumentPreparedStatementSetter(Object[] args) {
    this.args = args;
  }

  @Override
  public void setValues(PreparedStatement ps) throws SQLException {
    if (args != null) {
      for (int i = 1; i <= args.length; i++) {
        ps.setObject(i, args[i - 1]);

      }
    }
  }
}
