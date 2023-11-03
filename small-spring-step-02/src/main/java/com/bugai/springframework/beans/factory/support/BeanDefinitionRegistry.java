package com.bugai.springframework.beans.factory.support;

import com.bugai.springframework.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {
  void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
