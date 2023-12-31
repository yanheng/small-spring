package com.bugai.springframework.beans.factory;

import com.bugai.springframework.beans.BeansException;

public interface ObjectFactory<T> {
  /**
   * Return an instance (possibly shared or independent)
   * of the object managed by this factory.
   * @return the resulting instance
   * @throws BeansException in case of creation errors
   */
  T getObject() throws BeansException;
}
