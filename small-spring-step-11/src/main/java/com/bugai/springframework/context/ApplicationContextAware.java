package com.bugai.springframework.context;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.factory.Aware;

public interface ApplicationContextAware extends Aware {
  /**
   * Set the ApplicationContext that this object runs in.
   * Normally this call will be used to initialize the object.
   * Invoked after population of normal bean properties but before an init
   * callback such as org.springframework.beans.factory.InitializingBean.afterPropertiesSet()
   * or a custom init-method. Invoked after ResourceLoaderAware.setResourceLoader,
   * ApplicationEventPublisherAware.setApplicationEventPublisher and MessageSourceAware, if applicable.
   *
   * @param applicationContext
   * @throws BeansException
   */
  void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
