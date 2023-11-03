package com.bugai.springframework.aop.support;

import cn.hutool.core.lang.Assert;
import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.factory.BeanFactory;
import com.bugai.springframework.beans.factory.BeanFactoryAware;
import org.aopalliance.aop.Advice;

public abstract class AbstractBeanFactoryPointcutAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

  private String adviceBeanName;

  private BeanFactory beanFactory;

  private transient volatile Advice advice;

  private transient volatile Object adviceMonitor = new Object();


  public String getAdviceBeanName() {
    return adviceBeanName;
  }

  public void setAdviceBeanName(String adviceBeanName) {
    this.adviceBeanName = adviceBeanName;
  }

  public void setAdvice(Advice advice) {
    synchronized (this.adviceMonitor) {
      this.advice = advice;
    }
  }

  @Override
  public Advice getAdvice() {
    Advice advice = this.advice;
    if (null != advice) {
      return advice;
    }
    Assert.state(this.adviceBeanName != null, "'adviceBeanName' must be specified");
    Assert.state(this.beanFactory != null, "BeanFactory must be set to resolve 'adviceBeanName'");

    advice = this.beanFactory.getBean(this.adviceBeanName, Advice.class);
    this.advice = advice;

    return advice;
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
    resetAdviceMonitor();
  }

  private void resetAdviceMonitor() {
    adviceMonitor = new Object();
  }
}
