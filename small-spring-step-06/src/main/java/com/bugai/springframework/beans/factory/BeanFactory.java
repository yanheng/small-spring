package com.bugai.springframework.beans.factory;

import com.bugai.springframework.beans.BeansException;

public interface BeanFactory {

  Object getBean(String name) throws BeansException;

  Object getBean(String name, Object... args) throws BeansException;

  <T> T getBean(String name, Class<T> clazz) throws BeansException;
}
