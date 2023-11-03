package com.bugai.springframework.test.event;

import com.bugai.springframework.context.ApplicationListener;
import com.bugai.springframework.context.event.ContextClosedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {

  @Override
  public void onApplicationEvent(ContextClosedEvent event) {
    System.out.println("关闭事件：" + this.getClass().getName());
  }
}
