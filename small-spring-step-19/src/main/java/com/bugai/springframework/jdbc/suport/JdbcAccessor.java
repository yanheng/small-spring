package com.bugai.springframework.jdbc.suport;

import com.bugai.springframework.beans.factory.support.InitializingBean;

import javax.sql.DataSource;

public abstract class JdbcAccessor implements InitializingBean {

  protected DataSource dataSource;

  public DataSource getDataSource() {
    return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  protected DataSource obtainDataSource() {
    return getDataSource();
  }

  @Override
  public void afterPropertySet() {
    if (null == getDataSource()) {
      throw new IllegalArgumentException("Property 'datasource' is required");
    }

  }
}
