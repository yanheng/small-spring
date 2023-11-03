package com.bugai.springframework.context.support;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.context.ConfigurableApplicationContext;
import com.bugai.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.bugai.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.bugai.springframework.beans.factory.config.BeanPostProcessor;
import com.bugai.springframework.core.io.DefaultResourceLoader;

import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

  @Override
  public void refresh() {
    // 1. 创建 BeanFactory，并加载 BeanDefinition
    refreshBeanFactory();

    // 2. 获取 BeanFactory
    ConfigurableListableBeanFactory beanFactory = getBeanFactory();


    // 3. 在 Bean 实例化之前，执行 BeanFactoryPostProcessor
    invokeBeanFactoryPostProcessors(beanFactory);

    // 4. BeanPostProcessor 需要提前于其他 Bean 对象实例化之前执行注册操作
    registerBeanPostProcessors(beanFactory);
    // 2.1
    beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

    // 5. 提前实例化单例Bean对象
    beanFactory.preInstantiateSingletons();
  }

  abstract void refreshBeanFactory();

  abstract ConfigurableListableBeanFactory getBeanFactory();


  private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
    Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
    for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
      beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
    }
  }

  private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
    Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
    for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
      beanFactory.addBeanPostProcessor(beanPostProcessor);
    }
  }

  @Override
  public Object getBean(String name) {
    return getBeanFactory().getBean(name);
  }

  @Override
  public Object getBean(String name, Object... args) throws BeansException {
    return getBeanFactory().getBean(name, args);
  }

  public <T> T getBean(String name, Class<T> type) {
    return getBeanFactory().getBean(name, type);
  }

  @Override
  public void registerShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(this::close));
  }

  @Override
  public void close() {
    getBeanFactory().destroySingletons();
  }

  @Override
  public <T> Map<String, T> getBeansOfType(Class<T> type) {
    return getBeanFactory().getBeansOfType(type);
  }

  @Override
  public String[] getBeanDefinitionNames() {
    return getBeanFactory().getBeanDefinitionNames();
  }
}
