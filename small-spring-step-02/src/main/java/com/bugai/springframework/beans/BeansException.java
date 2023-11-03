package com.bugai.springframework.beans;

public class BeansException extends RuntimeException {
  public BeansException() {
    super();
  }

  public BeansException(String message) {
    super(message);
  }

  public BeansException(String message, Throwable cause) {
    super(message, cause);
  }

  public BeansException(Throwable cause) {
    super(cause);
  }

  protected BeansException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
