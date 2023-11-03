package com.bugai.springframework.jdbc.suport;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.bugai.springframework.jdbc.UncategorizedSQLException;
import com.bugai.springframework.jdbc.core.*;
import com.bugai.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class JdbcTemplate extends JdbcAccessor implements JdbcOperations {

  private int fetchSize = -1;

  private int maxRows = -1;

  private int queryTimeout = -1;


  public JdbcTemplate() {
  }

  public JdbcTemplate(DataSource dataSource) {
    setDataSource(dataSource);
    afterPropertySet();
  }

  public int getFetchSize() {
    return fetchSize;
  }

  public void setFetchSize(int fetchSize) {
    this.fetchSize = fetchSize;
  }

  public int getMaxRows() {
    return maxRows;
  }

  public void setMaxRows(int maxRows) {
    this.maxRows = maxRows;
  }

  public int getQueryTimeout() {
    return queryTimeout;
  }

  public void setQueryTimeout(int queryTimeout) {
    this.queryTimeout = queryTimeout;
  }

  @Override
  public <T> T execute(StatementCallback<T> action) {

    return execute(action, true);
  }

  private <T> T execute(StatementCallback<T> action, boolean closeResources) {
    // 创建连接
    Connection connection = DataSourceUtils.getConnection(obtainDataSource());
    Statement statement = null;

    try {
      // 创建statement
      statement = connection.createStatement();
      applyStatementSettings(statement);
      return action.doInStatement(statement);
    } catch (SQLException e) {
      String sql = getSql(action);
      JdbcUtils.closeStatement(statement);
      statement = null;
      throw translateException("ConnectionCallback", sql, e);
    } finally {
      if (closeResources) {
        JdbcUtils.closeStatement(statement);
      }
    }
  }

  protected void applyStatementSettings(Statement statement) throws SQLException {
    int fetchSize = getFetchSize();
    if (fetchSize != -1) {
      statement.setFetchSize(fetchSize);
    }
    int maxRows = getMaxRows();
    if (maxRows != -1) {
      statement.setMaxRows(maxRows);
    }
  }

  private static String getSql(Object sqlProvider) {
    if (sqlProvider instanceof SqlProvider) {
      return ((SqlProvider) sqlProvider).getSql();
    } else {
      return null;
    }
  }

  protected UncategorizedSQLException translateException(String task, String sql, SQLException ex) {
    return new UncategorizedSQLException(task, sql, ex);
  }

  @Override
  public void execute(String sql) {

    class ExecuteStatementCallback implements StatementCallback, SqlProvider {

      @Override
      public String getSql() {
        return sql;
      }

      @Override
      public Object doInStatement(Statement statement) throws SQLException {
        statement.execute(sql);
        return null;
      }
    }
    execute(new ExecuteStatementCallback(), true);
  }

  @Override
  public <T> T query(String sql, ResultSetExtractor<T> res) {
    Assert.notNull(sql, "SQL must not be null");
    Assert.notNull(res, "ResultSetExtractor must be null");

    class QueryStatementCallback implements StatementCallback<T>, SqlProvider {

      @Override
      public String getSql() {
        return sql;
      }

      @Override
      public T doInStatement(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(sql);
        return res.extractData(resultSet);
      }
    }
    return execute(new QueryStatementCallback(), true);
  }

  @Override
  public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse) {
    return query(new SimplePreparedStatementCreator(sql), newArgPreparedStatementSetter(args), rse);
  }

  @Override
  public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
    return query(sql, new RowMapperResultSetExtractor<>(rowMapper));
  }

  @Override
  public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) {
    return query(sql, args, new RowMapperResultSetExtractor<>(rowMapper));
  }

  @Override
  public <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse) {
    Assert.notNull(rse, "ResultSetExtractor must not be null");

    return query(new SimplePreparedStatementCreator(sql), pss, rse);
  }

  @Override
  public List<Map<String, Object>> queryForList(String sql) {
    return query(sql, new ColumnMapRowMapper());
  }

  @Override
  public <T> List<T> queryForList(String sql, Class<T> elementType) {
    return query(sql, new SingleColumnRowMapper<>(elementType));
  }

  @Override
  public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) {
    return query(sql, args, new SingleColumnRowMapper<>(elementType));
  }

  @Override
  public List<Map<String, Object>> queryForList(String sql, Object... args) {
    return query(sql, args, new ColumnMapRowMapper());
  }

  @Override
  public <T> T queryForObject(String sql, RowMapper<T> rowMapper) {
    List<T> results = query(sql, rowMapper);
    if (CollUtil.isEmpty(results)) {
      throw new RuntimeException("Incorrect result size: expected 1, actual 0");
    }
    if (results.size() > 1) {
      throw new RuntimeException("Incorrect result size: expected 1, actual " + results.size());
    }
    return results.iterator().next();
  }

  @Override
  public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) {
    List<T> results = query(sql, args, new RowMapperResultSetExtractor<>(rowMapper, 0));
    if (CollUtil.isEmpty(results)) {
      throw new RuntimeException("Incorrect result size: expected 1, actual 0");
    }
    if (results.size() > 1) {
      throw new RuntimeException("Incorrect result size: expected 1, actual " + results.size());
    }
    return results.iterator().next();
  }

  @Override
  public <T> T queryForObject(String sql, Class<T> requiredType) {
    return queryForObject(sql, getSingleColumnRowMapper(requiredType));
  }

  @Override
  public Map<String, Object> queryForMap(String sql) {
    return result(queryForObject(sql, new ColumnMapRowMapper()));
  }

  @Override
  public Map<String, Object> queryForMap(String sql, Object... args) {
    return result(queryForObject(sql, args, getColumnMapRowMapper()));
  }

  public <T> T query(PreparedStatementCreator preparedStatementCreator, final PreparedStatementSetter preparedStatementSetter, ResultSetExtractor<T> rse) {
    Assert.notNull(rse, "ResultSetExtractor must not be null");
    Connection connection = DataSourceUtils.getConnection(obtainDataSource());
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = preparedStatementCreator.createPreparedStatement(connection);
      afterPropertySet();
      PreparedStatementCallback<T> preparedStatementCallback = new PreparedStatementCallback<T>() {

        @Override
        public T doInPreparedStatement(PreparedStatement ps) throws SQLException {
          ResultSet rs = null;
          try {
            if (preparedStatementSetter != null) {
              preparedStatementSetter.setValues(ps);
            }
            rs = ps.executeQuery();
            return rse.extractData(rs);
          } finally {
            JdbcUtils.closeResultSet(rs);
          }
        }
      };
      return preparedStatementCallback.doInPreparedStatement(preparedStatement);
    } catch (SQLException e) {
      String sql = getSql(preparedStatementCreator);
      preparedStatementCreator = null;
      JdbcUtils.closeStatement(preparedStatement);
      preparedStatement = null;
      DataSourceUtils.releaseConnection(connection, getDataSource());
      connection = null;
      throw translateException("PreparedStatementCallback", sql, e);
    } finally {
      JdbcUtils.closeStatement(preparedStatement);
      DataSourceUtils.releaseConnection(connection, getDataSource());
    }
  }

  private static <T> T result(T result) {
    Assert.state(null != result, "No result");
    return result;
  }

  protected RowMapper<Map<String, Object>> getColumnMapRowMapper() {
    return new ColumnMapRowMapper();
  }

  protected <T> RowMapper<T> getSingleColumnRowMapper(Class<T> requiredType) {
    return new SingleColumnRowMapper<>(requiredType);
  }

  protected PreparedStatementSetter newArgPreparedStatementSetter(Object[] args) {
    return new ArgumentPreparedStatementSetter(args);
  }

  private static class SimplePreparedStatementCreator implements PreparedStatementCreator, SqlProvider {

    private final String sql;

    public SimplePreparedStatementCreator(String sql) {
      this.sql = sql;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
      return con.prepareStatement(sql);
    }

    @Override
    public String getSql() {
      return sql;
    }
  }
}
