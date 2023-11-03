package com.bugai.springframework.utils;

import com.bugai.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.Set;

public class ClassUtils {

  private ClassUtils() {

  }

  public static ClassLoader getDefaultClassLoader() {
    ClassLoader cl = null;

    try {
      cl = Thread.currentThread().getContextClassLoader();
    } catch (Exception ex) {
      // Cannot access thread context ClassLoader - falling back to system class loader...
    }

    if (cl == null) {
      cl = ClassUtils.class.getClassLoader();
    }
    return cl;
  }

  /**
   * Check whether the specified class is a CGLIB-generated class.
   *
   * @param clazz the class to check
   */
  public static boolean isCglibProxyClass(Class<?> clazz) {
    return (clazz != null && isCglibProxyClassName(clazz.getName()));
  }

  /**
   * Check whether the specified class name is a CGLIB-generated class.
   *
   * @param className the class name to check
   */
  public static boolean isCglibProxyClassName(String className) {
    return (className != null && className.contains("$$"));
  }

//  public static Set<Class<?>> scanPackageByAnnotation(String basePackage, Class clazz) {
//    String packagePath = basePackage.replace(".", "\\");
//    URL resource = getDefaultClassLoader().getResource(packagePath);
//    File file = new File(resource.getFile());
//    for (File listFile : file.listFiles()) {
//      if (listFile.isDirectory()) {
//
//      }
//    }
//
//    return null;
//  }
}
