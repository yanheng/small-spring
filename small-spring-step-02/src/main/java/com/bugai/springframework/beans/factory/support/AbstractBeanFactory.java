package com.bugai.springframework.beans.factory.support;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.factory.BeanFactory;
import com.bugai.springframework.beans.factory.config.BeanDefinition;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegister implements BeanFactory {
  @Override
  public Object getBean(String name) throws BeansException {
    Object bean = getSingleton(name);
    if (bean != null) {
      return bean;
    }
    BeanDefinition beanDefinition = getBeanDefinition(name);
    return createBean(name, beanDefinition);
  }

  protected abstract BeanDefinition getBeanDefinition(String name) throws BeansException;

  protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;

}
