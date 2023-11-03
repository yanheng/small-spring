package com.bugai.springframework.beans.factory.config;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.PropertyValues;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

  Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName);

  boolean postProcessAfterInstantiation(Object bean, String beanName);

  PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException;

  default Object getEarlyBeanReference(Object bean, String beanName) {
    return bean;
  }

}
