package com.bugai.springframework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;

public class TransactionProxy implements InvocationHandler {

  private final Connection connection;

  private final Object target;

  public TransactionProxy(Connection connection, Object target) {
    this.connection = connection;
    this.target = target;
  }


  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    connection.setAutoCommit(false);

    Object resValue = null;
    try {
      resValue = method.invoke(target, args);
    } catch (Exception e) {
      e.printStackTrace();
      connection.rollback();
    }
    connection.commit();
    return resValue;
  }
}
