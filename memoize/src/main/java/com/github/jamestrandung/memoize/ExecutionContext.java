package com.github.jamestrandung.memoize;

import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public String toString() {
    return String.format(TEMPLATE, this.targetClass.getName(), this.targetMethod, Arrays.toString(this.arguments));
  }
}
