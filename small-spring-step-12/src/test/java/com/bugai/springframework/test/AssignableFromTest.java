package com.bugai.springframework.test;

import com.bugai.springframework.beans.factory.BeanFactory;
import com.bugai.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.bugai.springframework.test.bean.IUserService;
import com.bugai.springframework.test.bean.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssignableFromTest {

  @Test
  public void testAssignableFrom(){
    boolean assignableFrom = IUserService.class.isAssignableFrom(UserService.class);
    Assertions.assertTrue(assignableFrom);

    boolean assignableFrom1 = BeanFactory.class.isAssignableFrom(DefaultListableBeanFactory.class);
    Assertions.assertTrue(assignableFrom1);
  }
}
