package com.bugai.springframework.test.bean;

import com.bugai.springframework.beans.factory.annotation.Autowired;
import com.bugai.springframework.beans.factory.annotation.Value;
import com.bugai.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class UserService implements IUserService {

  private String token;



  public String queryUserInfo() {
    try {
      Thread.sleep(new Random(1).nextInt(100));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return "aop token: " + token;
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
