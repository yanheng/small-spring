package com.bugai.springframework.test;

import com.bugai.springframework.beans.PropertyValue;
import com.bugai.springframework.beans.PropertyValues;
import com.bugai.springframework.beans.factory.config.BeanDefinition;
import com.bugai.springframework.beans.factory.config.BeanReference;
import com.bugai.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.bugai.springframework.test.bean.IUserDao;
import com.bugai.springframework.test.bean.UserService;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.Test;

public class ApiTest {

  @Test
  public void test_BeanFactory() {
    //1. 初始化 BeanFactory
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

    //2. 注册UserDao
    beanFactory.registerBeanDefinition("userDao", new BeanDefinition(IUserDao.class));

    //3. UserService 设置属性【uId, userDao】
    PropertyValues propertyValues = new PropertyValues();
    propertyValues.addPropertyValue(new PropertyValue("uid", "10001"));
    propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));

    //4. 注册userService
    BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
    beanFactory.registerBeanDefinition("userService", beanDefinition);

    //5. 获取bean userService
    UserService userService = (UserService) beanFactory.getBean("userService");
    userService.queryUserInfo();

  }

  @Test
  @SneakyThrows
  public void test_setProperty(){
    UserService userService = new UserService();
    BeanUtils.setProperty(userService, "uid", "123");
    System.out.println(userService.getUid());
  }
}
