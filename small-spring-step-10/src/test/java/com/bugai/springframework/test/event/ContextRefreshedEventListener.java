package com.bugai.springframework.test.event;

import com.bugai.springframework.context.ApplicationListener;
import com.bugai.springframework.context.event.ContextRefreshedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {


  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    log.info("上下文刷新了：");
  }
}
