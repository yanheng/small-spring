package com.bugai.springframework.beans.factory.support;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.factory.config.SingletonBeanRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DefaultSingletonBeanRegister implements SingletonBeanRegistry {

  protected static final Object NULL_OBJECT = new Object();

  private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

  private Map<String, Object> disposableBeans = new LinkedHashMap<>();


  @Override
  public Object getSingleton(String beanName) {
    return singletonObjects.get(beanName);
  }

  @Override
  public void registerSingleton(String beanName, Object singletonObject) {
    Object oldObject = singletonObjects.get(beanName);
    if (oldObject == null) {
      addSingleton(beanName, singletonObject);
    } else {
      throw new IllegalStateException("Could not register object [" + singletonObject +
              "] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
    }
  }

  protected void addSingleton(String beanName, Object singletonObject) {
    singletonObjects.put(beanName, singletonObject);
  }

  public void registerDisposableBean(String beanName, DisposableBean disposableBean) {
    this.disposableBeans.put(beanName, disposableBean);
  }

  public void destroySingletons() {
    Object[] disposableBeanNames = disposableBeans.keySet().toArray();
    for (int i = 0; i < disposableBeanNames.length; i++) {
      singletonObjects.remove(disposableBeanNames[i]);
      DisposableBean disposableBean = (DisposableBean) disposableBeans.remove(disposableBeanNames[i]);
      try {
        disposableBean.destroy();
      } catch (Exception e) {
        throw new BeansException("Destroy '" + disposableBeanNames[i] + "' exception");
      }
    }

  }

}
