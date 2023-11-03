package com.bugai.springframework.test.bean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService {

  private String uid;

  private String company;

  private String location;

  private IUserDao userDao;


  public void queryUserInfo() {
    log.info("查询用户信息：" + userDao.queryUserName(uid) + ", 公司：" + company + ", 地点" + location);
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public IUserDao getUserDao() {
    return userDao;
  }

  public void setUserDao(IUserDao userDao) {
    this.userDao = userDao;
  }
}
