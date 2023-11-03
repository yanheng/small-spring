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

public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

  private DefaultListableBeanFactory beanFactory;

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
    return bean;
  }

  @Override
  public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
    if (isInfrastructureClass(beanClass))
      return null;

    Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
    for (AspectJExpressionPointcutAdvisor advisor : advisors) {
      ClassFilter classFilter = advisor.getPointcut().getClassFilter();
      if (!classFilter.matches(beanClass))
        continue;

      AdvisedSupport advisedSupport = new AdvisedSupport();

      TargetSource targetSource = null;
      try {
        targetSource = new TargetSource(beanClass.getDeclaredConstructor().newInstance());
      } catch (Exception e) {
        e.printStackTrace();
      }
      advisedSupport.setTargetSource(targetSource);
      advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
      advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
      advisedSupport.setProxyTargetClass(false);
      return new ProxyFactory(advisedSupport).getProxy();
    }
    return null;

  }

  @Override
  public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
    return pvs;
  }

  private boolean isInfrastructureClass(Class<?> beanClass) {
    return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass);
  }
}
