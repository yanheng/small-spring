package com.bugai.springframework.tx.transaction.interceptor;

import java.io.Serializable;
import java.util.List;

public class RuleBasedTransactionAttribute extends DefaultTransactionAttribute implements Serializable {

  private List<RollbackRuleAttribute> rollbackRules;

  public RuleBasedTransactionAttribute() {
  }

  public void setRollbackRules(List<RollbackRuleAttribute> rollbackRules) {
    this.rollbackRules = rollbackRules;
  }
}
