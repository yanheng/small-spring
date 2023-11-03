package com.bugai.springframework.tx.transaction.interceptor;

import com.bugai.springframework.tx.transaction.support.DefaultTransactionDefinition;

public class DefaultTransactionAttribute extends DefaultTransactionDefinition implements TransactionAttribute {

  public DefaultTransactionAttribute() {
    super();
  }

  /**
   * 运行是异常或Error时返回true
   *
   * @param ex
   * @return
   */
  @Override
  public boolean rollbackOn(Throwable ex) {
    return (ex instanceof RuntimeException || ex instanceof Error);
  }

  @Override
  public String toString() {
    return "DefaultTransactionAttribute{}";
  }
}
