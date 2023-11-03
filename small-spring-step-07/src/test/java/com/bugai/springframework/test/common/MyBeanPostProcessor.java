package com.bugai.springframework.test.common;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.factory.config.BeanPostProcessor;
import com.bugai.springframework.test.bean.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyBeanPostProcessor implements BeanPostProcessor {
  @Override
  public Object postProcessorBeforeInitialization(Object bean, String beanName) throws BeansException {
    log.info("BeanPostProcessor execute....");
    if ("userService".equals(beanName)) {
      UserService userService = (UserService) bean;
      userService.setLocation("改为：北京");
    }
    return bean;
  }

  @Override
  public Object postProcessorAfterInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }
}
