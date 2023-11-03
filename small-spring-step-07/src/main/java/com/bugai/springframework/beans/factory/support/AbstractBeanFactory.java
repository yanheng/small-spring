package com.bugai.springframework.beans.factory.support;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.factory.BeanFactory;
import com.bugai.springframework.beans.factory.ConfigurableBeanFactory;
import com.bugai.springframework.beans.factory.config.BeanDefinition;
import com.bugai.springframework.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegister implements BeanFactory, ConfigurableBeanFactory {

  private List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

  @Override
  public Object getBean(String name) throws BeansException {
    return doGetBean(name, null);
  }

  @Override
  public Object getBean(String name, Object... args) throws BeansException {
    return doGetBean(name, args);
  }

  @Override
  public <T> T getBean(String name, Class<T> clazz) throws BeansException {
    return (T) doGetBean(name, null);
  }

  protected <T> T doGetBean(final String beanName, final Object[] args) {
    Object bean = getSingleton(beanName);
    if (bean != null) {
      return (T) bean;
    }
    BeanDefinition beanDefinition = getBeanDefinition(beanName);
    return (T) createBean(beanName, beanDefinition, args);
  }

  protected abstract BeanDefinition getBeanDefinition(String name) throws BeansException;

  protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;

  public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
    this.beanPostProcessors.remove(beanPostProcessor);
    this.beanPostProcessors.add(beanPostProcessor);
  }

  public void addBeanPostProcessor(Collection<? extends BeanPostProcessor> beanPostProcessors) {
    this.beanPostProcessors.removeAll(beanPostProcessors);
    this.beanPostProcessors.addAll(beanPostProcessors);
  }

  public List<BeanPostProcessor> getBeanPostProcessors() {
    return this.beanPostProcessors;
  }

}
