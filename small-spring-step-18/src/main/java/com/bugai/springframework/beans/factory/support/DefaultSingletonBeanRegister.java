package com.bugai.springframework.beans.factory.support;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.factory.ObjectFactory;
import com.bugai.springframework.beans.factory.config.SingletonBeanRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DefaultSingletonBeanRegister implements SingletonBeanRegistry {

  protected static final Object NULL_OBJECT = new Object();

  // 一级缓存，普通对象
  private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

  // 二级缓存，提前暴漏对象，没有完全实例化的对象
  protected final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>();

  // 三级缓存，存放代理对象
  private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<String, ObjectFactory<?>>();


  private Map<String, Object> disposableBeans = new LinkedHashMap<>();


  @Override
  public Object getSingleton(String beanName) {
    Object singletonObject = singletonObjects.get(beanName);
    if (singletonObject == null) {
      singletonObject = earlySingletonObjects.get(beanName);
      if (singletonObject == null) {
        ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
        if (singletonFactory != null) {
          // 把三级缓存中的代理对象中的真实对象获取出来，放入二级缓存中
          singletonObject = singletonFactory.getObject();
          earlySingletonObjects.put(beanName, singletonObject);
          singletonFactories.remove(beanName);
        }
      }
    }
    return singletonObject;
  }

  @Override
  public void registerSingleton(String beanName, Object singletonObject) {
    singletonObjects.put(beanName, singletonObject);
    earlySingletonObjects.remove(beanName);
    singletonFactories.remove(beanName);
  }

  protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
    if (!this.singletonObjects.containsKey(beanName)) {
      this.singletonFactories.put(beanName, singletonFactory);
      this.earlySingletonObjects.remove(beanName);
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
