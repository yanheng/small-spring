package com.bugai.springframework;

import com.alibaba.druid.pool.DruidDataSource;
import com.bugai.springframework.test.JdbcService;
import com.bugai.springframework.test.impl.JdbcServiceImpl;
import com.mysql.cj.jdbc.Driver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JdbcTest {

  private DruidDataSource dataSource;
  private Connection connection;
  private Statement statement;

  @BeforeAll
  public void init() throws SQLException {
    dataSource = new DruidDataSource();
    dataSource.setDriver(new Driver());
    dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/mybot?useUnicode=true&useSSL=false");
    dataSource.setPassword("root");
    dataSource.setUsername("root");

    connection = dataSource.getConnection();
    statement = connection.createStatement();
  }
  @Test
  public void saveDataWithoutTranslation() throws SQLException {

    statement.execute("insert into teacher(teacher_name) values ('李老师')");

    statement.execute("insert into user(id,username) values(1,'重复数据')");

  }

  @Test
  public void saveDataWithTranslation() throws SQLException {

    connection.setAutoCommit(false);
    try {
      statement.execute("insert into teacher(teacher_name) values ('王老师')");

      statement.execute("insert into user(id,username) values(1,'重复数据')");
    } catch (Exception e) {
      e.printStackTrace();
      connection.rollback();
    }

    //        connection.commit();
    System.out.println("=====");
  }

  @Test
  public void saveDataWithTranslationProxy() throws SQLException {
    JdbcService jdbcService=new JdbcServiceImpl(statement);

    TransactionProxy transactionProxy=new TransactionProxy(connection,jdbcService);

    JdbcService jdbcServiceProxy = (JdbcService) Proxy.newProxyInstance(jdbcService.getClass().getClassLoader(),
            jdbcService.getClass().getInterfaces(), transactionProxy);

    jdbcServiceProxy.saveDataWithTranslation();
  }


  @AfterAll
  public void destroy() throws SQLException {
    if (null != statement) {
      statement.close();
    }
    if (null != connection) {
      connection.close();
    }
    if (null != dataSource) {
      dataSource.close();
    }
  }
}
