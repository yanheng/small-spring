package com.bugai.springframework.beans;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {
  private List<PropertyValue> propertyValues = new ArrayList<>();

  public PropertyValue[] getPropertyValues() {
    return this.propertyValues.toArray(new PropertyValue[0]);
  }

  public void addPropertyValue(PropertyValue pv) {
    this.propertyValues.add(pv);
  }

  public PropertyValue getPropertyValue(String propertyName) {
    for (PropertyValue pv : this.propertyValues) {
      if (pv.getName().equals(propertyName)) {
        return pv;
      }
    }
    return null;
  }
}
