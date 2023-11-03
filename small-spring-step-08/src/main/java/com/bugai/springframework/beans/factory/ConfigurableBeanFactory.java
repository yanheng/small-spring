package com.bugai.springframework.beans.factory;

import com.bugai.springframework.beans.factory.config.BeanPostProcessor;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory {
  String SCOPE_SINGLETON = "";
  String SCOPE_PROTOTYPE = "";

  void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

  void destroySingletons();

  ClassLoader getBeanClassLoader();

}
