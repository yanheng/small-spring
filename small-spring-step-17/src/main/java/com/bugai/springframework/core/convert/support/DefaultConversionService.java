package com.bugai.springframework.core.convert.support;

import com.bugai.springframework.core.convert.converter.ConverterRegistry;

public class DefaultConversionService extends GenericConversionService {

  public DefaultConversionService() {
    addDefaultConverters(this);
  }

  public static void addDefaultConverters(ConverterRegistry converterRegistry) {
    converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
  }
}
