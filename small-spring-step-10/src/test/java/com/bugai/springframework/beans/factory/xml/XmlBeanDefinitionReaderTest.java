package com.bugai.springframework.beans.factory.xml;

import com.bugai.springframework.context.support.ClassPathXmlApplicationContext;
import com.bugai.springframework.test.bean.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class XmlBeanDefinitionReaderTest {

  @Test
  public void test_xml() {
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
    applicationContext.registerShutdownHook();

    UserService userService1 = applicationContext.getBean("userService", UserService.class);
    UserService userService2 = applicationContext.getBean("userService", UserService.class);

    System.out.println(userService1);
    System.out.println(userService2);
    userService1.queryUserInfo();
    userService2.queryUserInfo();
  }

}