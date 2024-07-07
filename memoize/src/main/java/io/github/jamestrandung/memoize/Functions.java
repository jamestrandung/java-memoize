package io.github.jamestrandung.memoize;

public class Functions {
  private Functions() {
  }

  @FunctionalInterface
  public interface Func6<T1, T2, T3, T4, T5, T6, R> {
    R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);
  }

  @FunctionalInterface
  public interface Func5<T1, T2, T3, T4, T5, R> {
    R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);
  }

  @FunctionalInterface
  public interface Func4<T1, T2, T3, T4, R> {
    R apply(T1 t1, T2 t2, T3 t3, T4 t4);
  }

  @FunctionalInterface
  public interface Func3<T1, T2, T3, R> {
    R apply(T1 t1, T2 t2, T3 t3);
  }

  @FunctionalInterface
  public interface Func2<T1, T2, R> {
    R apply(T1 t1, T2 t2);
  }

  @FunctionalInterface
  public interface Func1<T1, R> {
    R apply(T1 t1);
  }

  @FunctionalInterface
  public interface Func<R> {
    R apply();
  }


  public record FunctionIdentity(Class<?> declaringType, String methodName) {
    public static FunctionIdentity of(Class<?> declaringType, String methodName) {
      return new FunctionIdentity(declaringType, methodName);
    }

    public ExecutionContext toExecutionContext(Object... args) {
      return new ExecutionContext(this.declaringType, this.methodName, args);
    }
  }
}
