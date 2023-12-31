package com.bugai.springframework.beans.factory.config;

import com.bugai.springframework.beans.BeansException;

public interface BeanPostProcessor {

  /**
   * 在 Bean 对象执行初始化方法之前，执行此方法
   * @param bean
   * @param beanName
   * @return
   * @throws BeansException
   */
  Object postProcessorBeforeInitialization(Object bean, String beanName) throws BeansException;

  /**
   * 在 Bean 对象执行初始化方法之后，执行此方法
   * @param bean
   * @param beanName
   * @return
   * @throws BeansException
   */
  Object postProcessorAfterInitialization(Object bean, String beanName) throws BeansException;
}
