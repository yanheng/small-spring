package com.bugai.springframework.test.bean;

import com.bugai.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ProxyBeanFactory implements FactoryBean<IUserDao> {

  @Override
  public IUserDao getObject() {
    InvocationHandler handler = (proxy, method, args) -> {
      if ("toString".equals(method.getName())) return this.toString();
      Map<String, String> hashMap = new HashMap<>();
      hashMap.put("10001", "小傅哥");
      hashMap.put("10002", "八杯水");
      hashMap.put("10003", "阿毛");

      return "你被代理了 " + method.getName()+ "：" + hashMap.get(args[0]);
    };
    return (IUserDao) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IUserDao.class}, handler);
  }

  @Override
  public Class<?> getObjectType() {
    return IUserDao.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

}
