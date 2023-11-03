package com.bugai.springframework.beans.factory;

public interface BeanNameAware extends Aware {
  /**
   * Set the name of the bean in the bean factory that created this bean.
   * Invoked after population of normal bean properties but before an
   * init callback such as InitializingBean.afterPropertiesSet() or a custom init-method.
   *
   * @param name
   */
  void setBeanName(String name);
}
