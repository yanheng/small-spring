package com.bugai.springframework.aop;

public interface ClassFilter {
  ClassFilter TRUE = TrueClassFilter.INSTANCE;
  boolean matches(Class<?> clazz);
}
