package com.bugai.springframework.beans.factory.support;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.factory.config.BeanDefinition;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

public class DisposableBeanAdapter implements DisposableBean {
  private final Object bean;

  private final String beanName;

  private String destroyMethodName;


  public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
    this.bean = bean;
    this.beanName = beanName;
    this.destroyMethodName = beanDefinition.getDestroyMethodName();
  }

  @Override
  public void destroy() throws Exception {
    if (bean instanceof DisposableBean) {
      ((DisposableBean) bean).destroy();
    }

    if (StringUtils.isNotBlank(destroyMethodName)) {
      Method method = bean.getClass().getMethod(destroyMethodName);
      if (null != method) {
        method.invoke(bean);
      } else {
        throw new BeansException("Couldn't find a destroy method named '" + destroyMethodName + "' on bean with name '" + beanName + "'");
      }
    }
  }
}
