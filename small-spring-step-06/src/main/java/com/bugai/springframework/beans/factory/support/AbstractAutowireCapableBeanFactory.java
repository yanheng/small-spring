package com.bugai.springframework.beans.factory.support;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.PropertyValue;
import com.bugai.springframework.beans.PropertyValues;
import com.bugai.springframework.beans.factory.AutowireCapableBeanFactory;
import com.bugai.springframework.beans.factory.config.BeanDefinition;
import com.bugai.springframework.beans.factory.config.BeanPostProcessor;
import com.bugai.springframework.beans.factory.config.BeanReference;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Constructor;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

  private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

  @Override
  protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
    Object bean = null;
    try {
      // 实例化bean的class对象
      bean = createInstance(beanDefinition, beanName, args);
      // 填充属性
      this.applyPropertyValues(beanName, bean, beanDefinition);
      // 初始化
      initializeBean(beanName, bean, beanDefinition);
    } catch (Exception e) {
      throw new BeansException("Instantiation of bean failed", e);
    }
    addSingleton(beanName, bean);
    return bean;
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
    //1. 执行 BeanPostProcessor Before 处理
    Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
    invokeInitMethods(beanName, wrappedBean, beanDefinition);
    wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
    return wrappedBean;
  }

  private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) {
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
