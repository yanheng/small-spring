package com.bugai.springframework;

import com.bugai.springframework.test.bean.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BeanFactoryTest {

  @Test
  public void test_BeanFactory() {
    BeanFactory factory = new BeanFactory();

    UserService userService1 = new UserService();
    BeanDefinition beanDefinition = new BeanDefinition(userService1);

    factory.register("userService", beanDefinition);

    UserService userService = (UserService) factory.getBean("userService");
    userService.queryUserService();
    Assertions.assertSame(userService1, userService);

  }
}