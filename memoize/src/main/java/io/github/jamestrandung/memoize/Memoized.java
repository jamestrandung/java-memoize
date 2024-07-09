package io.github.jamestrandung.memoize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * This annotation marks a type or a method as a candidate for memoization so that repeated executions of the annotated method and of
 * any Spring-proxied methods of the annotated type will not run more than once per memoize scope.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Memoized {
  @Aspect
  class MemoizeAspect {
    @Pointcut("@target(io.github.jamestrandung.memoize.Memoized) && execution(* *(..))")
    public void methodInMemoizedType() {
    }

    @Pointcut("execution(@io.github.jamestrandung.memoize.Memoized * *.*(..))")
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
