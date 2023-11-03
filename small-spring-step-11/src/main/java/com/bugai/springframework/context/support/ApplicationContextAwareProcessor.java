package com.bugai.springframework.context.support;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.context.ApplicationContext;
import com.bugai.springframework.context.ApplicationContextAware;
import com.bugai.springframework.beans.factory.config.BeanPostProcessor;

public class ApplicationContextAwareProcessor implements BeanPostProcessor {

  private final ApplicationContext applicationContext;

  public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public Object postProcessorBeforeInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof ApplicationContextAware) {
      ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
    }
    return bean;
  }

  @Override
  public Object postProcessorAfterInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }
}
