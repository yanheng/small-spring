package com.bugai.springframework;

import com.bugai.springframework.core.annotation.AliasFor;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
public class AliasForTest extends SuperClass {

  @Test
  public void test(){
    boolean annotation = AliasFor.class.isAnnotation();
    System.out.println(annotation);

    Annotation[] declaredAnnotations = AliasForTest.class.getDeclaredAnnotations();
    for (int i = 0; i < declaredAnnotations.length; i++) {
      System.out.println(declaredAnnotations[i]);
    }

    Aspect annotation1 = AliasForTest.class.getAnnotation(Aspect.class);
    System.out.println(annotation1);

    System.out.println("=====================");
    Annotation[] annotations = AliasForTest.class.getAnnotations();
    for (int i = 0; i < annotations.length; i++) {
      System.out.println(annotations[i]);
    }

    System.out.println("===============");
    Method[] declaredMethods = AliasForTest.class.getDeclaredMethods();
    for (int i = 0; i < declaredMethods.length; i++) {
      Annotation[] declaredAnnotations1 = declaredMethods[i].getDeclaredAnnotations();
      System.out.println(declaredAnnotations1[i]);
    }
  }
}
