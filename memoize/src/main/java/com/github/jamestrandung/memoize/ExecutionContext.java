package com.github.jamestrandung.memoize;

import java.util.Arrays;
import java.util.Objects;

/**
 * ExecutionContext takes target method's arguments into account. Hence, if one or more parameters are POJO, those POJOs must handle
 * equal() and hashCode() properly so that the context of each target method invocation can be recorded & compared correctly.
 *
 * @param targetClass
 * @param targetMethod
 * @param arguments
 */
public record ExecutionContext(Class<?> targetClass, String targetMethod, Object[] arguments) {
  private static final String TEMPLATE = "%s.%s(%s)";

  @Override
  public boolean equals(Object that) {
    if (that instanceof ExecutionContext casted) {
      return this.doEquals(casted);
    }

    return false;
  }

  private boolean doEquals(ExecutionContext that) {
    if (!Objects.equals(this.targetClass, that.targetClass)) {
      return false;
    }

    if (!Objects.equals(this.targetMethod, that.targetMethod)) {
      return false;
    }

    return Arrays.equals(this.arguments, that.arguments);
  }

  @Override
  public int hashCode() {
    int result = 1;

    result = 31 * result + (this.targetClass == null ? 0 : this.targetClass.hashCode());
    result = 31 * result + (this.targetMethod == null ? 0 : this.targetMethod.hashCode());
    result = 31 * result + (this.arguments == null ? 0 : Arrays.hashCode(this.arguments));

    return result;
  }

  @Override
  public String toString() {
    return String.format(TEMPLATE, this.targetClass.getName(), this.targetMethod, Arrays.toString(this.arguments));
  }
}
