package com.bugai.springframework.test.bean;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class UserService implements IUserService {
  public String queryUserInfo() {
    try {
      Thread.sleep(new Random(1).nextInt(100));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return "小傅哥，100001，深圳";
  }

  public String register(String userName) {
    try {
      Thread.sleep(new Random(1).nextInt(100));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return "注册用户：" + userName + " success！";
  }
}
