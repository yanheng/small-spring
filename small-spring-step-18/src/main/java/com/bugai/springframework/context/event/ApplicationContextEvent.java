package com.bugai.springframework.context.event;

import com.bugai.springframework.context.ApplicationContext;
import com.bugai.springframework.context.ApplicationEvent;

public class ApplicationContextEvent extends ApplicationEvent {
  /**
   * Constructs a prototypical Event.
   *
   * @param source The object on which the Event initially occurred.
   * @throws IllegalArgumentException if source is null.
   */
  public ApplicationContextEvent(Object source) {
    super(source);
  }

  public final ApplicationContext getApplicationContext() {
    return (ApplicationContext) getSource();
  }
}
