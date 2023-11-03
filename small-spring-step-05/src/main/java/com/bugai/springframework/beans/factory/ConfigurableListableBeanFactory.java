package com.bugai.springframework.beans.factory;

import com.bugai.springframework.beans.factory.config.BeanDefinition;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, ConfigurableBeanFactory, AutowireCapableBeanFactory {

  BeanDefinition getBeanDefinition(String beanName);
}
