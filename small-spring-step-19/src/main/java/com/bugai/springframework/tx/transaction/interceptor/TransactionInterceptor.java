package com.bugai.springframework.tx.transaction.interceptor;

import com.bugai.springframework.tx.transaction.PlatformTransactionManager;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.io.Serializable;

public class TransactionInterceptor extends TransactionAspectSupport implements MethodInterceptor, Serializable {

  public TransactionInterceptor(PlatformTransactionManager ptm, TransactionAttributeSource transactionAttributeSource) {
    setTransactionManager(ptm);
    setTransactionAttributeSource(transactionAttributeSource);
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    return invokeWithinTransaction(invocation.getMethod(), invocation.getThis().getClass(), invocation::proceed);
  }
}
