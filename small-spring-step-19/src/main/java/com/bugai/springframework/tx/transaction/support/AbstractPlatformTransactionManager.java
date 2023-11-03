package com.bugai.springframework.tx.transaction.support;

import com.bugai.springframework.tx.transaction.PlatformTransactionManager;
import com.bugai.springframework.tx.transaction.TransactionDefinition;
import com.bugai.springframework.tx.transaction.TransactionException;
import com.bugai.springframework.tx.transaction.TransactionStatus;

import java.io.Serializable;

public abstract class AbstractPlatformTransactionManager implements PlatformTransactionManager, Serializable {

  private int defaultTimeout = TransactionDefinition.TIMEOUT_DEFAULT;


  @Override
  public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
    Object transaction = doGetTransaction();
    if (null == definition) {
      definition = new DefaultTransactionDefinition();
    }

    if (definition.getTimeout() < TransactionDefinition.TIMEOUT_DEFAULT) {
      throw new TransactionException("Invalid transaction timeout " + definition.getTimeout());
    }

    try {
      DefaultTransactionStatus status = newTransactionStatus(definition, transaction, true);
      doBegin(transaction, definition);
      return status;
    } catch (RuntimeException | Error ex) {
      throw ex;
    }

  }

  protected DefaultTransactionStatus newTransactionStatus(TransactionDefinition definition,
                                                          Object transaction, boolean newTransaction) {

    return new DefaultTransactionStatus(transaction, newTransaction);
  }

  protected int determineTimeout(TransactionDefinition definition) {
    if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT) {
      return definition.getTimeout();
    }
    return this.defaultTimeout;
  }

  @Override
  public void commit(TransactionStatus status) throws TransactionException {
    if (status.isCompleted()) {
      throw new IllegalArgumentException(
              "Transaction is already completed - do not call or rollback more than once per transaction");
    }
    DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
    processCommit(defStatus);
  }

  private void processCommit(DefaultTransactionStatus status) throws TransactionException {
    doCommit(status);
  }

  @Override
  public void rollback(TransactionStatus status) throws TransactionException {
    if (status.isCompleted()) {
      throw new IllegalArgumentException(
              "Transaction is already completed - do not call commit or rollback more than once per transaction");
    }
    DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
    processRollback(defStatus, false);
  }

  private void processRollback(DefaultTransactionStatus status, boolean unexpected) {
    doRollback(status);
  }


  //---------------------------------------------------------------------
  // Abstract methods to be implemented by subclasses start
  //---------------------------------------------------------------------

  /**
   * 获取事务
   */
  protected abstract Object doGetTransaction() throws TransactionException;

  /**
   * 提交事务
   */
  protected abstract void doCommit(DefaultTransactionStatus status) throws TransactionException;

  /**
   * 事务回滚
   */
  protected abstract void doRollback(DefaultTransactionStatus status) throws TransactionException;

  /**
   * 开始事务
   */
  protected abstract void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException;

  //---------------------------------------------------------------------
  // Abstract methods to be implemented by subclasses end
  //---------------------------------------------------------------------

}