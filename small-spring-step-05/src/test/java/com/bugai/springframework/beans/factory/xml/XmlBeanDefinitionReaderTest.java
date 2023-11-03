package com.bugai.springframework.beans.factory.xml;

import com.bugai.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.bugai.springframework.beans.test.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XmlBeanDefinitionReaderTest {

  @Test
  public void test_xmlBeanDefinitionReader(){
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
    reader.loadBeanDefinitions("classpath:spring.xml");

    UserService userService = beanFactory.getBean("userService", UserService.class);
    userService.queryUserInfo();
  }

}