package com.bugai.springframework.aop;

import com.bugai.springframework.utils.ClassUtils;

public class TargetSource {

  private final Object target;

  public TargetSource(Object target) {
    this.target = target;
  }

  public Class<?>[] getTargetClass() {
    Class<?> clazz = this.target.getClass();
    clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;
    return clazz.getInterfaces();
  }

  public Object getTarget() {
    return target;
  }

}
