package com.bugai.springframework.beans.factory;

public interface BeanClassLoaderAware extends Aware {
  /**
   * Callback that supplies the bean class loader to a bean instance.
   * Invoked after the population of normal bean properties but before an
   * initialization callback such as InitializingBean's
   * InitializingBean.afterPropertiesSet() method or a custom init-method.
   *
   * @param classLoader
   */
  void setBeanClassLoader(ClassLoader classLoader);
}
