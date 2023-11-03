package com.bugai.springframework.test.impl;

import com.bugai.springframework.jdbc.suport.JdbcTemplate;
import com.bugai.springframework.test.JdbcService;
import com.bugai.springframework.tx.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Statement;

public class JdbcServiceImpl implements JdbcService {
  private Statement statement;

  public JdbcServiceImpl() {
  }

  public JdbcServiceImpl(Statement statement) {
    this.statement = statement;
  }

  @Override
  public void saveDataWithTranslation() throws SQLException {
    statement.execute("insert into teacher(teacher_name) values ('赵老师')");

    statement.execute("insert into user(id,username) values(1,'重复数据1')");
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void saveData(JdbcTemplate jdbcTemplate) {
    jdbcTemplate.execute("insert into teacher(teacher_name) values ('李老师')");
    jdbcTemplate.execute("insert into user(id,username) values(1,'重复数据')");
  }
}
