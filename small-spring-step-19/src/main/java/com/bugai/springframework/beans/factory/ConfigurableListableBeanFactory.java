package com.bugai.springframework.beans.factory;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.factory.config.BeanDefinition;
import com.bugai.springframework.beans.factory.config.BeanFactoryPostProcessor;

import java.util.Map;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, ConfigurableBeanFactory, AutowireCapableBeanFactory {

  BeanDefinition getBeanDefinition(String beanName);

  void preInstantiateSingletons() throws BeansException;

  default void invokeBeanFactoryPostProcessors() {
    Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = getBeansOfType(BeanFactoryPostProcessor.class);
    for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
      beanFactoryPostProcessor.postProcessBeanFactory(this);
    }
  }
}
