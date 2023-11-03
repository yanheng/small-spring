package com.bugai.springframework.context.event;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.context.ApplicationEvent;
import com.bugai.springframework.context.ApplicationListener;
import com.bugai.springframework.beans.factory.BeanFactory;
import com.bugai.springframework.beans.factory.BeanFactoryAware;
import com.bugai.springframework.utils.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {
  public final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

  private BeanFactory beanFactory;

  @Override
  public void addApplicationEvent(ApplicationListener<?> listener) {
    applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
  }

  @Override
  public void removeApplicationListener(ApplicationListener<?> listener) {
    applicationListeners.remove(listener);
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  protected Collection<ApplicationListener> getApplicationListeners(ApplicationEvent event) {
    LinkedHashSet<ApplicationListener> allListeners = new LinkedHashSet<>();
    for (ApplicationListener<ApplicationEvent> listener : applicationListeners) {
      if (supportsEvent(listener, event))
        allListeners.add(listener);
    }
    return allListeners;
  }

  protected boolean supportsEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event) {
    Class<? extends ApplicationListener> listenerClass = applicationListener.getClass();

    Class<?> targetClass = ClassUtils.isCglibProxyClass(listenerClass) ? listenerClass.getSuperclass() : listenerClass;
    Type genericInterface = targetClass.getGenericInterfaces()[0];

    Type actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
    String className = actualTypeArgument.getTypeName();
    Class<?> eventClassName;
    try {
      eventClassName = Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new BeansException("wrong event class name: " + className);
    }
    return eventClassName.isAssignableFrom(event.getClass());
  }
}
