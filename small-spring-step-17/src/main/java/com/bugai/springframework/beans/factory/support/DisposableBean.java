package com.bugai.springframework.beans.factory.support;

public interface DisposableBean {
  void destroy() throws Exception;
}
