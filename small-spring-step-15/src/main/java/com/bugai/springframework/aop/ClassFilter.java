package com.bugai.springframework.aop;

public interface ClassFilter {
  boolean matches(Class<?> clazz);
}
