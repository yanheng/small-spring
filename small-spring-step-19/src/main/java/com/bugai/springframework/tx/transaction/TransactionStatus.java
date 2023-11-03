package com.bugai.springframework.tx.transaction;

import java.io.Flushable;
import java.io.IOException;

public interface TransactionStatus extends SavepointManager, Flushable {
  /**
   * 是否开启新的事务
   */
  boolean isNewTransaction();

  boolean hasSavepoint();

  void setRollbackOnly();

  boolean isRollbackOnly();

  @Override
  void flush() throws IOException;

  boolean isCompleted();
}
