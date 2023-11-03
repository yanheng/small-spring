package com.bugai.springframework.tx.transaction.support;

import com.bugai.springframework.tx.transaction.TransactionDefinition;

import java.io.Serializable;

public class DefaultTransactionDefinition implements TransactionDefinition, Serializable {

  private int propagationBehavior = PROPAGATION_REQUIRED;

  private int isolationLevel = ISOLATION_DEFAULT;

  private int timeout = TIMEOUT_DEFAULT;

  private boolean readOnly = false;

  private String name;


  public DefaultTransactionDefinition() {
  }

  public DefaultTransactionDefinition(TransactionDefinition other) {
    this.propagationBehavior = other.getPropagationBehavior();
    this.isolationLevel = other.getIsolationLevel();
    this.timeout = other.getTimeout();
    this.readOnly = other.isReadOnly();
    this.name = other.getName();
  }

  @Override
  public int getPropagationBehavior() {
    return propagationBehavior;
  }

  @Override
  public int getIsolationLevel() {
    return isolationLevel;
  }

  @Override
  public int getTimeout() {
    return timeout;
  }

  @Override
  public boolean isReadOnly() {
    return readOnly;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setPropagationBehavior(int propagationBehavior) {
    this.propagationBehavior = propagationBehavior;
  }

  public void setIsolationLevel(int isolationLevel) {
    this.isolationLevel = isolationLevel;
  }

  public void setTimeOut(int timeout) {
    if (timeout < TIMEOUT_DEFAULT) {
      throw new IllegalArgumentException("Timeout must be a positive integer or TIMEOUT_DEFAULT");
    }
    this.timeout = timeout;
  }

  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  public void setName(String name) {
    this.name = name;
  }
}
