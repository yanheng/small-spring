package com.bugai.springframework.beans.factory;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.PropertyValue;
import com.bugai.springframework.beans.PropertyValues;
import com.bugai.springframework.beans.factory.config.BeanDefinition;
import com.bugai.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.bugai.springframework.core.io.DefaultResourceLoader;
import com.bugai.springframework.core.io.Resource;
import com.bugai.springframework.utils.StringValueResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * 填充字符串， 占位符字符替换为属性值
 *
 */
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

  public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

  public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

  private String location;

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    try {
      DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
      Resource resource = resourceLoader.getResource(location);
      Properties properties = new Properties();
      properties.load(resource.getInputStream());

      String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
      for (String beanName : beanDefinitionNames) {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);

        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
          Object value = propertyValue.getValue();
          if (!(value instanceof String)) {
            continue;
          }
          String strVal = (String) value;
          value = resolvePlaceholder(strVal, properties);
          propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), value));
        }
        StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(properties);
        beanFactory.addEmbeddedValueResolver(valueResolver);

      }
    } catch (IOException e) {
      throw new BeansException("Could not load properties", e);
    }
  }

  public String resolvePlaceholder(String strVal, Properties properties) {
    StringBuilder builder = new StringBuilder(strVal);
    int startIdx = strVal.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
    int stopIdx = strVal.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);

    if (startIdx != -1 && stopIdx != -1 && startIdx < stopIdx) {
      String propKey = strVal.substring(startIdx + 2, stopIdx);
      String propVal = properties.getProperty(propKey);
      builder.replace(startIdx, stopIdx + 1, propVal);
    }
    return builder.toString();
  }

  private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

    private final Properties properties;

    public PlaceholderResolvingStringValueResolver(Properties properties) {
      this.properties = properties;
    }

    @Override
    public String resolveStringValue(String strVal) {
      return PropertyPlaceholderConfigurer.this.resolvePlaceholder(strVal, properties);
    }
  }

  public void setLocation(String location) {
    this.location = location;
  }


}
