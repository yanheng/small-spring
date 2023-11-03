package com.bugai.springframework.beans.factory;

import com.bugai.springframework.beans.factory.config.BeanPostProcessor;
import com.bugai.springframework.beans.factory.config.SingletonBeanRegistry;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {
  String SCOPE_SINGLETON = "singleton";
  String SCOPE_PROTOTYPE = "prototype";

  void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

  void destroySingletons();

  ClassLoader getBeanClassLoader();

}
