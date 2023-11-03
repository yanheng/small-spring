package com.bugai.springframework.tx.transaction.annotation;

import com.bugai.springframework.tx.transaction.interceptor.TransactionAttribute;

import java.lang.reflect.AnnotatedElement;

public interface TransactionAnnotationParser {

  /**
   * 解析 事务注解
   */
  TransactionAttribute parseTransactionAnnotation(AnnotatedElement element);

}
