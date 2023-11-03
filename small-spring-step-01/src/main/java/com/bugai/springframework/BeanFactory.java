package com.bugai.springframework;

import java.util.HashMap;
import java.util.Map;

public class BeanFactory {
  private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

  public Object getBean(String name) {
    return beanDefinitionMap.get(name).getBean();
  }

  public void register(String name, BeanDefinition beanDefinition) {
    beanDefinitionMap.put(name, beanDefinition);
  }
}
