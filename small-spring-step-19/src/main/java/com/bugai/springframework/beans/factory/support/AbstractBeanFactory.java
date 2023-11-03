package com.bugai.springframework.beans.factory.support;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.factory.BeanFactory;
import com.bugai.springframework.beans.factory.ConfigurableBeanFactory;
import com.bugai.springframework.beans.factory.FactoryBean;
import com.bugai.springframework.beans.factory.config.BeanDefinition;
import com.bugai.springframework.beans.factory.config.BeanPostProcessor;
import com.bugai.springframework.core.convert.ConversionService;
import com.bugai.springframework.utils.ClassUtils;
import com.bugai.springframework.utils.StringValueResolver;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

  private List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

  private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

  private List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();

  private ConversionService conversionService;


  @Override
  public Object getBean(String name) throws BeansException {
    return doGetBean(name, null);
  }

  @Override
  public Object getBean(String name, Object... args) throws BeansException {
    return doGetBean(name, args);
  }

  @Override
  public <T> T getBean(String name, Class<T> clazz) throws BeansException {
    return (T) doGetBean(name, null);
  }

  protected <T> T doGetBean(final String beanName, final Object[] args) {
    Object sharedInstance = getSingleton(beanName);
    if (sharedInstance != null) {
      return (T) getObjectForBeanInstance(sharedInstance, beanName);
    }
    BeanDefinition beanDefinition = getBeanDefinition(beanName);
    log.info(beanName);
    Object bean = createBean(beanName, beanDefinition, args);
    return (T) getObjectForBeanInstance(bean, beanName);
  }

  private Object getObjectForBeanInstance(Object beanInstance, String beanName) {
    if (!(beanInstance instanceof FactoryBean)) {
      return beanInstance;
    }
    Object object = getCachedObjectForFactoryBean(beanName);
    if (object == null) {
      FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
      object = getObjectFromFactoryBean(factoryBean, beanName);
    }
    return object;
  }

  @Override
  public boolean containsBean(String name) {
    return containsBeanDefinition(name);
  }

  protected abstract BeanDefinition getBeanDefinition(String name) throws BeansException;

  protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;

  protected abstract boolean containsBeanDefinition(String beanName);


  public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
    this.beanPostProcessors.remove(beanPostProcessor);
    this.beanPostProcessors.add(beanPostProcessor);
  }

  public void addBeanPostProcessor(Collection<? extends BeanPostProcessor> beanPostProcessors) {
    this.beanPostProcessors.removeAll(beanPostProcessors);
    this.beanPostProcessors.addAll(beanPostProcessors);
  }

  public List<BeanPostProcessor> getBeanPostProcessors() {
    return this.beanPostProcessors;
  }

  @Override
  public ClassLoader getBeanClassLoader() {
    return beanClassLoader;
  }

  @Override
  public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
    this.embeddedValueResolvers.add(valueResolver);
  }

  @Override
  public String resolveEmbeddedValue(String value) {
    String result = value;
    for (StringValueResolver embeddedValueResolver : this.embeddedValueResolvers) {
      result = embeddedValueResolver.resolveStringValue(value);
    }
    return result;
  }

  @Override
  public void setConversionService(ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public ConversionService getConversionService() {
    return conversionService;
  }
}
