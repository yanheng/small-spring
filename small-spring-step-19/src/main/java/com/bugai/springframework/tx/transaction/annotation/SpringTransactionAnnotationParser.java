package com.bugai.springframework.tx.transaction.annotation;

import com.bugai.springframework.core.annotation.AnnotatedElementUtils;
import com.bugai.springframework.core.annotation.AnnotationAttributes;
import com.bugai.springframework.tx.transaction.interceptor.RollbackRuleAttribute;
import com.bugai.springframework.tx.transaction.interceptor.RuleBasedTransactionAttribute;
import com.bugai.springframework.tx.transaction.interceptor.TransactionAttribute;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

public class SpringTransactionAnnotationParser implements TransactionAnnotationParser {
  @Override
  public TransactionAttribute parseTransactionAnnotation(AnnotatedElement element) {
    AnnotationAttributes attributes = AnnotatedElementUtils.findMergedAnnotationAttributes(element, Transactional.class, false, false);
    if (null != attributes) {
      return parseTransactionAnnotation(attributes);
    }
    return null;
  }

  protected TransactionAttribute parseTransactionAnnotation(AnnotationAttributes attributes) {
    RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
    List<RollbackRuleAttribute> rollbackRules = new ArrayList<>();
    for (Class<?> rollbackFor : attributes.getClassArray("rollbackFor")) {
      rollbackRules.add(new RollbackRuleAttribute(rollbackFor));
    }

    // TODO 解些其他rollback或noRollback
    rbta.setRollbackRules(rollbackRules);
    return rbta;
  }
}
