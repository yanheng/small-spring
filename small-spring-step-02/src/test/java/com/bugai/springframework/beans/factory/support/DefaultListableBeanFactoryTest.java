package com.bugai.springframework.beans.factory.support;

import com.bugai.springframework.beans.factory.config.BeanDefinition;
import com.bugai.springframework.test.bean.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultListableBeanFactoryTest {


  @Test
  public void test_BeanFactory(){
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

    BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
    beanFactory.registerBeanDefinition("userService", beanDefinition);

    UserService userService = (UserService) beanFactory.getBean("userService");
    userService.queryUserService();

    UserService userService_singleton  = (UserService) beanFactory.getBean("userService");
    userService_singleton .queryUserService();

    Assertions.assertSame(userService, userService_singleton);
  }


}