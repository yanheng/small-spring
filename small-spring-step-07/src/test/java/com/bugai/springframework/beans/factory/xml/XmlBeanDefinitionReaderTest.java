package com.bugai.springframework.beans.factory.xml;

import com.bugai.springframework.context.support.ClassPathXmlApplicationContext;
import com.bugai.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.bugai.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.bugai.springframework.test.bean.UserService;
import com.bugai.springframework.test.common.MyBeanFactoryPostProcessor;
import com.bugai.springframework.test.common.MyBeanPostProcessor;
import org.junit.jupiter.api.Test;

class XmlBeanDefinitionReaderTest {

  @Test
  public void test_xmlBeanDefinitionReader() {
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
    reader.loadBeanDefinitions("classpath:spring.xml");

    BeanFactoryPostProcessor beanFactoryPostProcessor = new MyBeanFactoryPostProcessor();
    beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);

    MyBeanPostProcessor myBeanPostProcessor = new MyBeanPostProcessor();
    beanFactory.addBeanPostProcessor(myBeanPostProcessor);

    UserService userService = beanFactory.getBean("userService", UserService.class);
    userService.queryUserInfo();
  }

  @Test
  public void test_xml() {
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
    applicationContext.registerShutdownHook();

    UserService userService = applicationContext.getBean("userService", UserService.class);
    userService.queryUserInfo();
  }

}