package com.bugai.springframework.beans.factory.config;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

  Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName);
}
