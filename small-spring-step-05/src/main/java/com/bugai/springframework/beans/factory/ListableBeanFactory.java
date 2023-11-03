package com.bugai.springframework.beans.factory;

import java.util.Set;

public interface ListableBeanFactory extends BeanFactory {

  <T> T getBeanOfType(Class<T> type);

  Set<String> getBeanDefinitionNames();
}
