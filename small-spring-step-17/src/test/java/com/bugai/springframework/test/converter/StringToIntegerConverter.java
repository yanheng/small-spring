package com.bugai.springframework.test.converter;

import com.bugai.springframework.core.convert.converter.Converter;

public class StringToIntegerConverter implements Converter<String, Integer> {
  @Override
  public Integer convert(String source) {
    return Integer.valueOf(source);
  }
}
