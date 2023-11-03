package com.bugai.springframework.context.support;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.context.ApplicationEvent;
import com.bugai.springframework.context.ApplicationListener;
import com.bugai.springframework.context.ConfigurableApplicationContext;
import com.bugai.springframework.context.event.ApplicationEventMulticaster;
import com.bugai.springframework.context.event.ContextClosedEvent;
import com.bugai.springframework.context.event.ContextRefreshedEvent;
import com.bugai.springframework.context.event.SimpleApplicationEventMulticaster;
import com.bugai.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.bugai.springframework.beans.factory.config.BeanPostProcessor;
import com.bugai.springframework.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

  private static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

  private ApplicationEventMulticaster applicationEventMulticaster;

  @Override
  public void refresh() {
    // 1. 创建 BeanFactory，并加载 BeanDefinition
    refreshBeanFactory();

    // 2. 获取 BeanFactory
    ConfigurableListableBeanFactory beanFactory = getBeanFactory();

    // 2.1
    beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

    // 3. 在 Bean 实例化之前，执行 BeanFactoryPostProcessor
    beanFactory.invokeBeanFactoryPostProcessors();

    // 4. BeanPostProcessor 需要提前于其他 Bean 对象实例化之前执行注册操作
    registerBeanPostProcessors(beanFactory);

    // 5. 提前实例化单例Bean对象
    beanFactory.preInstantiateSingletons();

    // 初始化事件传播者
    initApplicationEventMulticaster();

    // 注册事件监听器， 到事件转播者
    registerListeners();

    // 刷新事件
    finishRefresh();

  }

  private void initApplicationEventMulticaster() {
    ConfigurableListableBeanFactory beanFactory = getBeanFactory();
    applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
    beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
  }

  private void registerListeners() {
    Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
    for (ApplicationListener listener : applicationListeners) {
      applicationEventMulticaster.addApplicationEvent(listener);
    }
  }

  private void finishRefresh() {
    publishEvent(new ContextRefreshedEvent(this));
  }


  abstract void refreshBeanFactory();

  abstract ConfigurableListableBeanFactory getBeanFactory();


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

  @Override
  public <T> T getBean(String name, Class<T> type) {
    return getBeanFactory().getBean(name, type);
  }


  @Override
  public <T> T getBean(Class<T> requiredType) throws BeansException {
    return getBeanFactory().getBean(requiredType);
  }

  @Override
  public void registerShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(this::close));
  }

  @Override
  public void close() {
    publishEvent(new ContextClosedEvent(this));
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

  @Override
  public void publishEvent(ApplicationEvent event) {
    applicationEventMulticaster.multicastEvent(event);
  }
}
