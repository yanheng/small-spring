package com.bugai.springframework.aop;

public interface Pointcut {

  ClassFilter getClassFilter();

  MethodMatcher getMethodMatcher();

}
