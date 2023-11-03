package com.bugai.springframework.beans.factory.support;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.PropertyValue;
import com.bugai.springframework.beans.PropertyValues;
import com.bugai.springframework.beans.factory.*;
import com.bugai.springframework.beans.factory.config.BeanDefinition;
import com.bugai.springframework.beans.factory.config.BeanPostProcessor;
import com.bugai.springframework.beans.factory.config.BeanReference;
import com.bugai.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

  private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

  @Override
  protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
    Object bean = null;
    try {
      bean = resolveBeforeInstantiation(beanName, beanDefinition);
      if (bean != null) {
        return bean;
      }
      // 实例化bean的class对象
      bean = createInstance(beanDefinition, beanName, args);
      // 填充属性
      this.applyPropertyValues(beanName, bean, beanDefinition);
      // 初始化
      initializeBean(beanName, bean, beanDefinition);
    } catch (Exception e) {
      throw new BeansException("Instantiation of bean failed", e);
    }
    registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);
    if (beanDefinition.isSingleton()) {
      addSingleton(beanName, bean);
    }
    return bean;
  }

  protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
    Object bean = applyBeanPostProcessorBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
    if (bean != null) {
      bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
    }
    return bean;
  }

  public Object applyBeanPostProcessorBeforeInstantiation(Class<?> beanClass, String beanName) {
    for (BeanPostProcessor processor : getBeanPostProcessors()) {
      if (processor instanceof InstantiationAwareBeanPostProcessor) {
        Object result = ((InstantiationAwareBeanPostProcessor) processor).postProcessBeforeInstantiation(beanClass, beanName);
        if (null != result) {
          return result;
        }
      }
    }
    return null;
  }

  protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {

    if (!beanDefinition.isSingleton())
      return;

    if (bean instanceof DisposableBean || StringUtils.isNotBlank(beanDefinition.getDestroyMethodName())) {
      registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
    }
  }


  protected Object createInstance(BeanDefinition beanDefinition, String beanName, Object[] args) {
    Constructor constructor = null;
    Class<?> beanClass = beanDefinition.getBeanClass();
    Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
    for (Constructor ctor : declaredConstructors) {
      if (null != args && ctor.getParameterTypes().length == args.length) {
        constructor = ctor;
        break;
      }
    }
    return getInstantiationStrategy().instantiate(beanDefinition, beanName, constructor, args);
  }

  protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {

    try {
      PropertyValues propertyValues = beanDefinition.getPropertyValues();
      for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
        String name = propertyValue.getName();
        Object value = propertyValue.getValue();
        if (value instanceof BeanReference) {
          BeanReference beanReference = (BeanReference) value;
          value = getBean(beanReference.getBeanName());
        }
        BeanUtils.setProperty(bean, name, value);
      }
    } catch (Exception e) {
      throw new BeansException("Error setting property values：" + beanName);
    }
  }

  public InstantiationStrategy getInstantiationStrategy() {
    return instantiationStrategy;
  }

  public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
    this.instantiationStrategy = instantiationStrategy;
  }

  @Override
  public Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) throws BeansException {

    if (bean instanceof Aware) {
      if (bean instanceof BeanFactoryAware) {
        ((BeanFactoryAware) bean).setBeanFactory(this);
      }
      if (bean instanceof BeanClassLoaderAware) {
        ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
      }
      if (bean instanceof BeanNameAware) {
        ((BeanNameAware) bean).setBeanName(beanName);
      }
    }

    //1. 执行 BeanPostProcessor Before 处理
    Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
    try {
      invokeInitMethods(beanName, wrappedBean, beanDefinition);
    } catch (Exception e) {
      throw new BeansException("Invocation of init method of bean[" + beanName + "] failed", e);
    }
    wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
    return wrappedBean;
  }

  private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
    //1. 实现 InitializingBean
    if (bean instanceof InitializingBean) {
      ((InitializingBean) bean).afterPropertySet();
    }

    String initMethodName = beanDefinition.getInitMethodName();
    if (StringUtils.isNotBlank(initMethodName)) {
      Method method = beanDefinition.getBeanClass().getMethod(initMethodName);
      if (null == method) {
        throw new BeansException("Could not find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
      }
      method.invoke(bean);
    }

  }

  @Override
  public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
    Object result = existingBean;
    for (BeanPostProcessor processor : getBeanPostProcessors()) {
      Object current = processor.postProcessorBeforeInitialization(result, beanName);
      if (null == current) {
        return result;
      } else {
        result = current;
      }
    }
    return result;
  }

  @Override
  public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
    Object result = existingBean;
    for (BeanPostProcessor processor : getBeanPostProcessors()) {
      Object current = processor.postProcessorAfterInitialization(result, beanName);
      if (null == current) {
        return result;
      } else {
        result = current;
      }
    }
    return result;
  }
}
