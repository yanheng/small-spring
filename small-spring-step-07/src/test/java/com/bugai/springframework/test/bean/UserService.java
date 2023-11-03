package com.bugai.springframework.test.bean;

import com.bugai.springframework.beans.factory.support.DisposableBean;
import com.bugai.springframework.beans.factory.support.InitializingBean;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService implements InitializingBean, DisposableBean {

  private String uid;

  private String company;

  private String location;

  private UserDao userDao;


  public void queryUserInfo() {
    log.info("查询用户信息：" + userDao.queryUserName(uid) + ", 公司：" + company + ", 地点" + location);
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public UserDao getUserDao() {
    return userDao;
  }

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
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

  @Override
  public void destroy() throws Exception {
    log.info("UserService.destroy");
  }

  @Override
  public void afterPropertySet() throws Exception {
    log.info("UserService.afterPropertiesSet");
  }
}
