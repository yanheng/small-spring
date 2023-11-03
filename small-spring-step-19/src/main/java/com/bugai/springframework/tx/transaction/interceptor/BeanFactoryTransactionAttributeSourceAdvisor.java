package com.bugai.springframework.tx.transaction.interceptor;

import com.bugai.springframework.aop.Pointcut;
import com.bugai.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

public class BeanFactoryTransactionAttributeSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {

  private TransactionAttributeSource transactionAttributeSource;

  public BeanFactoryTransactionAttributeSourceAdvisor() {
  }

  private final TransactionAttributeSourcePointcut pointcut = new TransactionAttributeSourcePointcut() {
    @Override
    protected TransactionAttributeSource getTransactionAttributeSource() {
      return transactionAttributeSource;
    }
  };

  public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource) {
    this.transactionAttributeSource = transactionAttributeSource;
  }

  @Override
  public Pointcut getPointcut() {
    return this.pointcut;
  }
}
