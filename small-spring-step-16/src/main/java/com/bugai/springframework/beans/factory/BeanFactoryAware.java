package com.bugai.springframework.beans.factory;

import com.bugai.springframework.beans.BeansException;

public interface BeanFactoryAware extends Aware {
  /**
   * Callback that supplies the owning factory to a bean instance.
   * Invoked after the population of normal bean properties but before
   * an initialization callback such as InitializingBean.afterPropertiesSet()
   * or a custom init-method.
   *
   * @param beanFactory
   * @throws BeansException
   */
  void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
