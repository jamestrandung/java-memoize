package io.github.jamestrandung.memoize;

import java.util.Arrays;
import java.util.Objects;

/**
 * ExecutionContext takes target method's arguments into account. Hence, if one or more parameters are POJO, those POJOs must handle
 * equal() and hashCode() properly so that the context of each target method invocation can be recorded and compared correctly.
 *
 * @param declaringType the class that owns the method that is about to be executed
 * @param methodName    the name of the method that is about to be executed
 * @param arguments     the arguments for the method that is about to be executed
 */
public record ExecutionContext(Class<?> declaringType, String methodName, Object[] arguments) {
  private static final String TEMPLATE = "%s.%s(%s)";

  @Override
  public boolean equals(Object that) {
    if (that instanceof ExecutionContext casted) {
      return this.doEquals(casted);
    }

    return false;
  }

  private boolean doEquals(ExecutionContext that) {
    if (!Objects.equals(this.declaringType, that.declaringType)) {
      return false;
    }

    if (!Objects.equals(this.methodName, that.methodName)) {
      return false;
    }

    return Arrays.equals(this.arguments, that.arguments);
  }

  @Override
  public int hashCode() {
    int result = 1;

    result = 31 * result + (this.declaringType == null ? 0 : this.declaringType.hashCode());
    result = 31 * result + (this.methodName == null ? 0 : this.methodName.hashCode());
    result = 31 * result + (this.arguments == null ? 0 : Arrays.hashCode(this.arguments));

    return result;
  }

  @Override
  public String toString() {
    return String.format(TEMPLATE, this.declaringType.getName(), this.methodName, Arrays.toString(this.arguments));
  }
}
