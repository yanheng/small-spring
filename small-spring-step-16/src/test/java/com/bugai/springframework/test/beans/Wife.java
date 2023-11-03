package com.bugai.springframework.test.beans;

public class Wife {
  private Husband husband;
  private IMother mother; // 婆婆

  public Husband getHusband() {
    return husband;
  }

  public void setHusband(Husband husband) {
    this.husband = husband;
  }

  public IMother getMother() {
    return mother;
  }

  public void setMother(IMother mother) {
    this.mother = mother;
  }

  public String queryHusband() {
    return "Wife.husband、Mother.callMother：" + mother.callMother();
  }
}
