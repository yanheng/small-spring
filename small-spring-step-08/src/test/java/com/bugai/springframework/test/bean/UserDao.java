package com.bugai.springframework.test.bean;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserDao {

  private static Map<String, String> hashMap = new HashMap<>();

  public void initDataMethod() {
    log.info("执行：init-method");
    hashMap.put("10001", "小傅哥");
    hashMap.put("10002", "八杯水");
    hashMap.put("10003", "阿毛");
  }

  public void destroyDataMethod(){
    log.info("执行：destroy-method");
    hashMap.clear();
  }

  public String queryUserName(String uId) {
    return hashMap.get(uId);
  }

}
