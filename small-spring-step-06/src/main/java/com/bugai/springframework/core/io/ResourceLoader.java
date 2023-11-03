package com.bugai.springframework.core.io;

public interface ResourceLoader {

  String CLASS_PATH_PREFIX = "classpath:";

  Resource getResource(String location);
}
