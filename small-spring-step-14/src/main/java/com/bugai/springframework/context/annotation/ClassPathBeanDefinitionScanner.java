package com.bugai.springframework.context.annotation;

import com.bugai.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.bugai.springframework.beans.factory.config.BeanDefinition;
import com.bugai.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.bugai.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {
  private BeanDefinitionRegistry registry;

  public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
    this.registry = registry;
  }

  public void doScan(String... basePackages) {
    for (String basePackage : basePackages) {
      Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
      for (BeanDefinition beanDefinition : candidates) {
        String scope = resolveBeanScope(beanDefinition);
        if (StringUtils.isNotBlank(scope)) {
          beanDefinition.setScope(scope);
        }
        registry.registerBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
      }
    }

    registry.registerBeanDefinition("com.bugai.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor", new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
  }

  private String resolveBeanScope(BeanDefinition beanDefinition) {
    Class<?> beanClass = beanDefinition.getBeanClass();
    Scope scope = beanClass.getAnnotation(Scope.class);
    if (scope != null)
      return scope.value();
    return null;
  }

  private String determineBeanName(BeanDefinition beanDefinition) {
    Class beanClass = beanDefinition.getBeanClass();
    Component component = (Component) beanClass.getAnnotation(Component.class);
    String value = component.value();
    if (StringUtils.isBlank(value)) {
      char[] chars = beanClass.getSimpleName().toCharArray();
      chars[0] = Character.toLowerCase(chars[0]);
      value = String.valueOf(chars);
    }
    return value;
  }
}
