package com.bugai.springframework.tx.transaction.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
@Documented
public @interface Transactional {
  Class<? extends Throwable>[] rollbackFor() default {};
}
