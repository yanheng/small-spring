package com.bugai.springframework.aop.aspectj;

import com.bugai.springframework.aop.Pointcut;
import com.bugai.springframework.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

  // 切面
  private AspectJExpressionPointcut pointcut;

  // 具体的拦截方法
  private Advice advice;

  // 表达式
  private String expression;

  public AspectJExpressionPointcutAdvisor() {
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  @Override
  public Advice getAdvice() {
    return advice;
  }

  @Override
  public Pointcut getPointcut() {
    if (pointcut == null) {
      return new AspectJExpressionPointcut(expression);
    }
    return pointcut;
  }

  public void setAdvice(Advice advice) {
    this.advice = advice;
  }
}
