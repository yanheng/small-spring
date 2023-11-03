package com.bugai.springframework.tx.transaction.interceptor;

import com.bugai.springframework.aop.ClassFilter;
import com.bugai.springframework.aop.MethodMatcher;
import com.bugai.springframework.aop.Pointcut;
import com.bugai.springframework.tx.transaction.PlatformTransactionManager;

import java.lang.reflect.Method;

public abstract class TransactionAttributeSourcePointcut implements MethodMatcher, Pointcut {

  private ClassFilter classFilter = ClassFilter.TRUE;


  @Override
  public boolean matches(Method method, Class<?> clazz) {
    if (PlatformTransactionManager.class.isAssignableFrom(clazz)) {
      return false;
    }

    TransactionAttributeSource tas = getTransactionAttributeSource();

    return null == tas || tas.getTransactionAttribute(method, clazz) != null;
  }

  @Override
  public ClassFilter getClassFilter() {
    return classFilter;
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return this;
  }

  @Override
  public boolean matches(Method method, Class<?> targetClass, Object... args) {
    return false;
  }



  //---------------------------------------------------------------------
  // Abstract methods to be implemented by subclasses start
  //---------------------------------------------------------------------

  protected abstract TransactionAttributeSource getTransactionAttributeSource();

  //---------------------------------------------------------------------
  // Abstract methods to be implemented by subclasses end
  //---------------------------------------------------------------------
}
