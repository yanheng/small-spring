package com.bugai.springframework.test;

import com.bugai.springframework.aop.MethodMatcher;
import com.bugai.springframework.aop.aspectj.AspectJExpressionPointcut;
import com.bugai.springframework.context.support.ClassPathXmlApplicationContext;
import com.bugai.springframework.test.bean.IUserService;
import com.bugai.springframework.test.bean.UserService;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ApiTest {


  @Test
  public void test_proxy_method() {
    Object targetObj = new UserService();
    IUserService proxy = (IUserService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), targetObj.getClass().getInterfaces(), new InvocationHandler() {

      MethodMatcher methodMatcher = new AspectJExpressionPointcut("execution(* com.bugai.springframework.test.bean.IUserService.*(..))");

      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (methodMatcher.matches(method, targetObj.getClass())) {
          MethodInterceptor methodInterceptor = invocation -> {
            long start = System.currentTimeMillis();
            try {
              return invocation.proceed();
            } finally {
              System.out.println("监控 - Begin By AOP");
              System.out.println("方法名称：" + invocation.getMethod().getName());
              System.out.println("方法耗时：" + (System.currentTimeMillis() - start) + "ms");
              System.out.println("监控 - End\r\n");
            }

          };
          //          return methodInterceptor.invoke(new ReflectiveMethodInvocation)
        }
        return method.invoke(targetObj, args);
      }
    });
    String result = proxy.queryUserInfo();
    System.out.println(result);
  }

  @Test
  public void test_aop() throws NoSuchMethodException {
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
    IUserService userService = applicationContext.getBean("userService", IUserService.class);
    System.out.println("测试结果：" + userService.queryUserInfo());
  }

}
