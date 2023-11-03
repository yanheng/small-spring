package com.bugai.springframework.test.bean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService {

  private String name;

  public UserService() {
  }

  public UserService(String name) {
    this.name = name;
  }

  public void queryUserService() {
    log.info("查询用户信息" + name);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("");
    sb.append("").append(name);
    return sb.toString();
  }
}
