package com.bugai.springframework;

import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.cj.jdbc.Driver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.*;

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
  public void createTable() throws SQLException {
    String sql = "create table if not exists ndcp_vendor( code varchar(100)   ,name varchar(100)   ,uuid varchar(72) not null  , PRIMARY KEY(uuid)) DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_bin";
    boolean execute = statement.execute(sql);
  }

  @Test
  public void insert() throws SQLException {
    String sql = "INSERT INTO ndcp_vendor (code, name, uuid) \n" +
            "VALUES \n" +
            "    ('VENDOR001', '供应商1', 'uuid-1'), \n" +
            "    ('VENDOR002', '供应商2', 'uuid-2'), \n" +
            "    ('VENDOR003', '供应商3', 'uuid-3')";
    statement.addBatch(sql);
    int[] ints = statement.executeBatch();
  }

  @Test
  public void selectTest() throws SQLException {
    String querySql = "select * from ndcp_vendor";

    ResultSet resultSet = statement.executeQuery(querySql);
    ResultSetMetaData metaData = resultSet.getMetaData();
    int columnCount = metaData.getColumnCount();
    while (resultSet.next()) {

      for (int i = 1; i <= columnCount; i++) {
        String columnName = metaData.getColumnName(i);
        String columnValue = resultSet.getString(i);
        log.info("columnName : {}, columnValue: {}", columnName, columnValue);
      }
    }
  }

  @Test
  public void preparedTest() throws SQLException {
    String querySql = "select * from ndcp_vendor where uuid=?";
    PreparedStatement preparedStatement = connection.prepareStatement(querySql);
    preparedStatement.setString(1, "uuid-1");
    ResultSet resultSet = preparedStatement.executeQuery();
    ResultSetMetaData metaData = resultSet.getMetaData();
    int columnCount = metaData.getColumnCount();
    while (resultSet.next()) {
      for (int i = 1; i <= columnCount; i++) {
        String columnName = metaData.getColumnName(i);
        String columnValue = resultSet.getString(i);
        log.info("columnName : {}, columnValue: {}", columnName, columnValue);
      }
    }
  }
}
