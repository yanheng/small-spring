package com.bugai.springframework.core.convert.converter;

@FunctionalInterface
public interface Converter<S, T> {
  /**
   * Convert the source object of type {@code S} to target type {@code T}.
   */
  T convert(S source);
}
