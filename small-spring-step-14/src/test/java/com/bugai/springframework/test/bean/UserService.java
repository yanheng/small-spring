package com.bugai.springframework.test.bean;

import com.bugai.springframework.beans.factory.annotation.Autowired;
import com.bugai.springframework.beans.factory.annotation.Value;
import com.bugai.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
@Component
public class UserService implements IUserService {

  @Value("${token}")
  private String token;

  @Autowired
  private UserDao userDao;


  public String queryUserInfo() {
    try {
      Thread.sleep(new Random(1).nextInt(100));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return userDao.queryUserName("10001") + "，" + token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UserDao getUserDao() {
    return userDao;
  }

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public String toString() {
    return "UserService#token = { " + token + " }";
  }
}
