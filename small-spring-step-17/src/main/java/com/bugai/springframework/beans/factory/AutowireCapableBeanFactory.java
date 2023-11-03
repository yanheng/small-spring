package com.bugai.springframework.beans.factory;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.factory.config.BeanDefinition;

public interface AutowireCapableBeanFactory extends BeanFactory {

  Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) throws BeansException;

  Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;

  Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;
}
