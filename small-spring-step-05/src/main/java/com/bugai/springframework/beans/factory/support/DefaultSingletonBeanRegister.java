package com.bugai.springframework.beans.factory.support;

import com.bugai.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

public class DefaultSingletonBeanRegister implements SingletonBeanRegistry {

  private Map<String, Object> singletonObjects = new HashMap<>();

  @Override
  public Object getSingleton(String beanName) {
    return singletonObjects.get(beanName);
  }

  protected void addSingleton(String beanName, Object singletonObject) {
    singletonObjects.put(beanName, singletonObject);
  }


}
