package com.bugai.springframework.context.event;

import com.bugai.springframework.context.ApplicationEvent;
import com.bugai.springframework.context.ApplicationListener;
import com.bugai.springframework.beans.factory.BeanFactory;

import java.util.Collection;

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

  public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
    setBeanFactory(beanFactory);
  }

  @Override
  public void multicastEvent(ApplicationEvent event) {
    Collection<ApplicationListener> applicationListeners = getApplicationListeners(event);
    for (ApplicationListener<ApplicationEvent> applicationListener : applicationListeners) {
      applicationListener.onApplicationEvent(event);
    }
  }
}
