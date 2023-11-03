package com.bugai.springframework.test;

import com.bugai.springframework.context.support.ClassPathXmlApplicationContext;
import com.bugai.springframework.test.bean.IUserService;
import org.junit.jupiter.api.Test;

public class ApiTest {
  @Test
  public void test_scan() {
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-scan.xml");
    IUserService userService = applicationContext.getBean("userService", IUserService.class);
    System.out.println("测试结果：" + userService.queryUserInfo());
  }
}
