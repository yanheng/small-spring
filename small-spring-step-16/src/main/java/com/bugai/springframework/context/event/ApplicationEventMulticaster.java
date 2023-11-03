package com.bugai.springframework.context.event;

import com.bugai.springframework.context.ApplicationEvent;
import com.bugai.springframework.context.ApplicationListener;

public interface ApplicationEventMulticaster {

  void addApplicationEvent(ApplicationListener<?> listener);

  void removeApplicationListener(ApplicationListener<?> listener);

  void multicastEvent(ApplicationEvent event);
}
