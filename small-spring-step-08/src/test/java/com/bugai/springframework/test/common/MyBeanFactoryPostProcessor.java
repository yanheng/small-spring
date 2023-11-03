package com.bugai.springframework.test.common;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.PropertyValue;
import com.bugai.springframework.beans.PropertyValues;
import com.bugai.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.bugai.springframework.beans.factory.config.BeanDefinition;
import com.bugai.springframework.beans.factory.config.BeanFactoryPostProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    log.info("BeanFactoryPostProcessor execute....");
    BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userService");
    PropertyValues propertyValues = beanDefinition.getPropertyValues();
    propertyValues.addPropertyValue(new PropertyValue("company", "改为：字节跳动"));
  }
}
