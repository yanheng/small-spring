package com.bugai.springframework.test.bean;

import com.bugai.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
@Component
public class UserService implements IUserService {
  private String token;

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

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public String toString() {
    return "UserService#token = { " + token + " }";
  }
}
