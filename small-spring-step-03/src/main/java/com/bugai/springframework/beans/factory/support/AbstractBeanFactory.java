package com.bugai.springframework.beans.factory.support;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.factory.BeanFactory;
import com.bugai.springframework.beans.factory.config.BeanDefinition;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegister implements BeanFactory {
  @Override
  public Object getBean(String name) throws BeansException {
    return doGetBean(name, null);
  }

  @Override
  public Object getBean(String name, Object... args) throws BeansException {
    return doGetBean(name, args);
  }

  protected <T> T doGetBean(final String beanName, final Object[] args) {
    Object bean = getSingleton(beanName);
    if (bean != null) {
      return (T)bean;
    }
    BeanDefinition beanDefinition = getBeanDefinition(beanName);
    return (T) createBean(beanName, beanDefinition, args);
  }

  protected abstract BeanDefinition getBeanDefinition(String name) throws BeansException;

  protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;


}
