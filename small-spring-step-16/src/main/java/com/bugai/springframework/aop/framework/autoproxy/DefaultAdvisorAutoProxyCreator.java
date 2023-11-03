package com.bugai.springframework.aop.framework.autoproxy;

import com.bugai.springframework.aop.*;
import com.bugai.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.bugai.springframework.aop.framework.ProxyFactory;
import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.PropertyValues;
import com.bugai.springframework.beans.factory.BeanFactory;
import com.bugai.springframework.beans.factory.BeanFactoryAware;
import com.bugai.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.bugai.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

  private DefaultListableBeanFactory beanFactory;

  private final Set<Object> earlyProxyReferences = Collections.synchronizedSet(new HashSet<Object>());


  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = (DefaultListableBeanFactory) beanFactory;
  }

  @Override
  public Object postProcessorBeforeInitialization(Object bean, String beanName) throws BeansException {

    return bean;
  }

  @Override
  public Object postProcessorAfterInitialization(Object bean, String beanName) throws BeansException {
    if (!earlyProxyReferences.contains(beanName)) {
      return wrapIfNecessary(bean, beanName);
    }
    return bean;
  }

  protected Object wrapIfNecessary(Object bean, String beanName) {
    if (isInfrastructureClass(bean.getClass()))
      return bean;
    Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
    for (AspectJExpressionPointcutAdvisor advisor : advisors) {
      ClassFilter classFilter = advisor.getPointcut().getClassFilter();
      if (!classFilter.matches(bean.getClass()))
        continue;

      AdvisedSupport advisedSupport = new AdvisedSupport();

      TargetSource targetSource = null;
      try {
        targetSource = new TargetSource(bean.getClass().getDeclaredConstructor().newInstance());
      } catch (Exception e) {
        e.printStackTrace();
      }
      advisedSupport.setTargetSource(targetSource);
      advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
      advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
      advisedSupport.setProxyTargetClass(true);
      return new ProxyFactory(advisedSupport).getProxy();
    }
    return bean;

  }

  @Override
  public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
    return null;
  }

  @Override
  public boolean postProcessAfterInstantiation(Object bean, String beanName) {
    return true;
  }

  @Override
  public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
    return pvs;
  }

  @Override
  public Object getEarlyBeanReference(Object bean, String beanName) {
    earlyProxyReferences.add(bean);
    return wrapIfNecessary(bean, beanName);
  }

  private boolean isInfrastructureClass(Class<?> beanClass) {
    return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass);
  }
}
