package com.bugai.springframework.tx.transaction.interceptor;

import com.bugai.springframework.tx.transaction.TransactionDefinition;

/**
 * 事务属性
 */
public interface TransactionAttribute extends TransactionDefinition {

  boolean rollbackOn(Throwable ex);

}
