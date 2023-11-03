package com.bugai.springframework.beans.factory;

import com.bugai.springframework.beans.factory.config.BeanPostProcessor;
import com.bugai.springframework.beans.factory.config.SingletonBeanRegistry;
import com.bugai.springframework.core.convert.ConversionService;
import com.bugai.springframework.utils.StringValueResolver;
import com.sun.istack.internal.Nullable;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {
  String SCOPE_SINGLETON = "singleton";
  String SCOPE_PROTOTYPE = "prototype";

  void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

  void destroySingletons();

  ClassLoader getBeanClassLoader();

  /**
   * Add a String resolver for embedded values such as annotation attributes.
   * @param valueResolver the String resolver to apply to embedded values
   * @since 3.0
   */
  void addEmbeddedValueResolver(StringValueResolver valueResolver);

  /**
   * Resolve the given embedded value, e.g. an annotation attribute.
   * @param value the value to resolve
   * @return the resolved value (may be the original value as-is)
   * @since 3.0
   */
  String resolveEmbeddedValue(String value);

  void setConversionService(ConversionService conversionService);

  @Nullable
  ConversionService getConversionService();

}
