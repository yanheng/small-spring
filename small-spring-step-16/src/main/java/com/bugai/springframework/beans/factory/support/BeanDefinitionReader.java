package com.bugai.springframework.beans.factory.support;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.core.io.Resource;
import com.bugai.springframework.core.io.ResourceLoader;

public interface BeanDefinitionReader {

  BeanDefinitionRegistry getRegister();

  ResourceLoader getResourceLoader();

  void loadBeanDefinitions(Resource resource) throws BeansException;

  void loadBeanDefinitions(Resource... resources) throws BeansException;

  void loadBeanDefinitions(String locations) throws BeansException;

  void loadBeanDefinitions(String... locations) throws BeansException;
}
