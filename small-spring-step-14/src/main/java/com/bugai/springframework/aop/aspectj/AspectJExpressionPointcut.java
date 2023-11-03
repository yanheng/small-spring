package com.bugai.springframework.aop.aspectj;

import com.bugai.springframework.aop.ClassFilter;
import com.bugai.springframework.aop.MethodMatcher;
import com.bugai.springframework.aop.Pointcut;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class AspectJExpressionPointcut implements Pointcut, MethodMatcher, ClassFilter {

  private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();

  static {
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
  }

  private transient PointcutExpression pointcutExpression;

  public AspectJExpressionPointcut(String expression){
    PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, this.getClass().getClassLoader());
    pointcutExpression = pointcutParser.parsePointcutExpression(expression);
  }

  @Override
  public boolean matches(Class<?> clazz) {
    return pointcutExpression.couldMatchJoinPointsInType(clazz);
  }

  @Override
  public boolean matches(Method method, Class<?> targetClass) {
    return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
  }

  @Override
  public boolean matches(Method method, Class<?> targetClass, Object... args) {
    return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
  }

  @Override
  public ClassFilter getClassFilter() {
    return this;
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return this;
  }
}
