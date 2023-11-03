package com.bugai.springframework.core.io;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceLoaderTest {

  private DefaultResourceLoader resourceLoader;

  @BeforeEach
  private void init() {
    resourceLoader = new DefaultResourceLoader();
  }

  @Test
  @SneakyThrows
  void test_ClassPathResource() {
    Resource resource = resourceLoader.getResource("classpath:application.properties");
    InputStream inputStream = resource.getInputStream();
    String content = IOUtils.toString(inputStream);
    assertTrue(StringUtils.isNotBlank(content));
  }

  @Test
  @SneakyThrows
  void test_fileResource() {
    Resource resource = resourceLoader.getResource("src/test/resources/application.properties");
    InputStream inputStream = resource.getInputStream();
    String content = IOUtils.toString(inputStream);
    assertTrue(StringUtils.isNotBlank(content));
  }

  @Test
  void test_urlResource() {
    // TODO
  }
}