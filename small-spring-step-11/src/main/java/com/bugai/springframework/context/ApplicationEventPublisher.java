package com.bugai.springframework.context;

public interface ApplicationEventPublisher {
  void publishEvent(ApplicationEvent event);
}
