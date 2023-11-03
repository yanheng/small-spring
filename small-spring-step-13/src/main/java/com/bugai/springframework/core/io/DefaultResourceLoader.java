package com.bugai.springframework.core.io;

import org.apache.commons.lang3.Validate;

import java.net.MalformedURLException;
import java.net.URL;

public class DefaultResourceLoader implements ResourceLoader {

  @Override
  public Resource getResource(String location) {
    Validate.notBlank(location, "Location must not be null");
    if (location.startsWith(CLASS_PATH_PREFIX)) {
      return new ClassPathResource(location.substring(CLASS_PATH_PREFIX.length()));
    } else {
      try {
        URL url = new URL(location);
        return new UrlResource(url);
      } catch (MalformedURLException e) {
        return new FileSystemResource(location);
      }
    }
  }
}
