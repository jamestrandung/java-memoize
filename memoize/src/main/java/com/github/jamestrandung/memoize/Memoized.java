package com.github.jamestrandung.memoize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Memoized {
  @Aspect
  class MemoizeAspect {
    @Pointcut("execution(* *(..)) && @target(com.github.jamestrandung.memoize.Memoized)")
    public void methodInMemoizedType() {
    }

    @Pointcut("execution(@com.github.jamestrandung.memoize.Memoized * *.*(..))")
    public void methodAnnotatedWithMemoized() {
    }

    @Around("methodInMemoizedType() || methodAnnotatedWithMemoized()")
    public Object memoize(ProceedingJoinPoint pjp) {
      return Memoize.execute(
          new ExecutionContext(
              pjp.getSignature().getDeclaringType(),
              pjp.getSignature().getName(),
              pjp.getArgs()
          ),
          pjp::proceed
      );
    }
  }
}
