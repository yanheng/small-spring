package com.bugai.springframework.test.beans;

public class Husband {
  private Wife wife;

  public Wife getWife() {
    return wife;
  }

  public void setWife(Wife wife) {
    this.wife = wife;
  }

  public String queryWife(){
    return "Husband.wife";
  }
}
