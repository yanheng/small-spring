package com.bugai.springframework.beans.factory.support;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegister {

  private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

  protected Object getCachedObjectForFactoryBean(String beanName) {
    Object object = this.factoryBeanObjectCache.get(beanName);
    return object != NULL_OBJECT ? object : null;
  }

  protected Object getObjectFromFactoryBean(FactoryBean factoryBean, String beanName) {
    if (factoryBean.isSingleton()) {
      Object o = this.factoryBeanObjectCache.get(beanName);
      if (o == null) {
        o = doGetObjectFromFactoryBean(factoryBean, beanName);
        this.factoryBeanObjectCache.put(beanName, (o != null ? o : NULL_OBJECT));
      }
      return o != NULL_OBJECT ? o : null;
    } else {
      return doGetObjectFromFactoryBean(factoryBean, beanName);
    }
  }

  protected Object doGetObjectFromFactoryBean(final FactoryBean factoryBean, final String beanName) {
    try {
      return factoryBean.getObject();
    } catch (Exception e) {
      throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation", e);
    }


  }

}
