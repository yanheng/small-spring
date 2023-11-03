package com.bugai.springframework.beans.factory.annotation;

import cn.hutool.core.bean.BeanUtil;
import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.PropertyValues;
import com.bugai.springframework.beans.factory.BeanFactory;
import com.bugai.springframework.beans.factory.BeanFactoryAware;
import com.bugai.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.bugai.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.bugai.springframework.utils.ClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

  private ConfigurableListableBeanFactory beanFactory;

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
  }

  @Override
  public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
    Class<?> aClass = bean.getClass();
    aClass = ClassUtils.isCglibProxyClass(aClass) ? aClass.getSuperclass() : aClass;

    Field[] declaredFields = aClass.getDeclaredFields();

    // Value注解
    for (Field field : declaredFields) {
      Value valueAnnotation = field.getAnnotation(Value.class);
      if (valueAnnotation != null) {
        String value = valueAnnotation.value();
        value = beanFactory.resolveEmbeddedValue(value);
        try {
          BeanUtils.setProperty(bean, field.getName(), value);
        } catch (IllegalAccessException | InvocationTargetException e) {
          log.error(e.getMessage(), e);
        }
      }
    }

    // Autowired
    for (Field field : declaredFields) {
      Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
      if (autowiredAnnotation != null) {
        Class<?> fieldType = field.getType();
        String dependentBeanName = null;
        Object dependentBean = null;
        Qualifier qualifierAnnotation = field.getAnnotation(Qualifier.class);
        if (qualifierAnnotation != null) {
          dependentBeanName = qualifierAnnotation.value();
          dependentBean = beanFactory.getBean(dependentBeanName, fieldType);
        } else {
          dependentBean = beanFactory.getBean(fieldType);
        }
        try {
          BeanUtils.setProperty(bean, field.getName(), dependentBean);
        } catch (IllegalAccessException | InvocationTargetException e) {
          log.error(e.getMessage(), e);
        }
      }
    }
    return pvs;
  }

  @Override
  public Object postProcessorBeforeInitialization(Object bean, String beanName) throws BeansException {
    return null;
  }

  @Override
  public Object postProcessorAfterInitialization(Object bean, String beanName) throws BeansException {
    return null;
  }

  @Override
  public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
    return null;
  }

  @Override
  public boolean postProcessAfterInstantiation(Object bean, String beanName) {
    return true;
  }


}
