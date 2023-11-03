package com.bugai.springframework.beans.factory.support;

public interface InitializingBean {

  void afterPropertySet() throws Exception;
}
