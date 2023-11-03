package com.bugai.springframework.beans.factory;

import com.bugai.springframework.beans.factory.config.BeanPostProcessor;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory {
  String SCOPE_SINGLETON = "singleton";
  String SCOPE_PROTOTYPE = "prototype";

  void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

  void destroySingletons();

  ClassLoader getBeanClassLoader();

}
