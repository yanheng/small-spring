package com.bugai.springframework.context.support;

import com.bugai.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.bugai.springframework.beans.factory.support.DefaultListableBeanFactory;

public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

  private DefaultListableBeanFactory beanFactory;


  @Override
  void refreshBeanFactory() {
    DefaultListableBeanFactory beanFactory = createBeanFactory();
    loadBeanDefinitions(beanFactory);
    this.beanFactory = beanFactory;
  }

  private DefaultListableBeanFactory createBeanFactory() {
    return new DefaultListableBeanFactory();
  }

  protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

  @Override
  ConfigurableListableBeanFactory getBeanFactory() {
    return beanFactory;
  }
}
