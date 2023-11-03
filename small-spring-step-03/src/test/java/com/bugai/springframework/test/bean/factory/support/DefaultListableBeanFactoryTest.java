package com.bugai.springframework.test.bean.factory.support;

import com.bugai.springframework.beans.factory.config.BeanDefinition;
import com.bugai.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.bugai.springframework.test.bean.UserService;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class DefaultListableBeanFactoryTest {


  @Test
  public void test_BeanFactory() {
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

    BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
    beanFactory.registerBeanDefinition("userService", beanDefinition);

    UserService userService = (UserService) beanFactory.getBean("userService");
    userService.queryUserService();

    UserService userService_singleton = (UserService) beanFactory.getBean("userService");
    userService_singleton.queryUserService();

    Assertions.assertSame(userService, userService_singleton);
  }

  @Test
  public void test_newInstance() throws InstantiationException, IllegalAccessException {
    Assertions.assertThrows(Exception.class, () -> {
      UserService.class.newInstance();
    });
  }

  @Test
  public void test_constructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    Class<UserService> userServiceClass = UserService.class;
    Constructor<UserService> declaredConstructor = userServiceClass.getDeclaredConstructor(String.class);
    UserService yanheng = declaredConstructor.newInstance("yanheng");
    System.out.println(yanheng);
  }

  @Test
  public void test_parameterTypes() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    Class<UserService> userServiceClass = UserService.class;
    Constructor<?>[] declaredConstructors = userServiceClass.getDeclaredConstructors();
    Constructor<?> constructor = declaredConstructors[0];
    Constructor<UserService> declaredConstructor = userServiceClass.getDeclaredConstructor(constructor.getParameterTypes());
    UserService userService = declaredConstructor.newInstance("闫衡");
    System.out.println(userService);
  }

  @Test
  public void test_cglib() throws InvocationTargetException, IllegalAccessException {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(UserService.class);
    enhancer.setCallback(new NoOp() {
      @Override
      public int hashCode() {
        return super.hashCode();
      }
    });

    UserService obj = (UserService) enhancer.create(new Class[]{String.class}, new Object[]{""});

    BeanUtils.setProperty(obj, "name", "闫衡");

    obj.queryUserService();
  }


}