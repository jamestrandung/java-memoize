package com.github.jamestrandung.memoize;

import com.github.jamestrandung.memoize.Functions.Func;
import com.github.jamestrandung.memoize.Functions.Func1;
import com.github.jamestrandung.memoize.Functions.Func2;
import com.github.jamestrandung.memoize.Functions.Func3;
import com.github.jamestrandung.memoize.Functions.Func4;
import com.github.jamestrandung.memoize.Functions.Func5;
import com.github.jamestrandung.memoize.Functions.Func6;
import com.github.jamestrandung.memoize.Functions.FunctionIdentity;

public class Memoize {
  private Memoize() {
  }

  public static <A1, A2, A3, A4, A5, A6, R> R execute(
      FunctionIdentity fnIdentity,
      Func6<A1, A2, A3, A4, A5, A6, R> fn,
      A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5, A6 arg6
  ) {
    return execute(
        fnIdentity.toExecutionContext(arg1, arg2, arg3, arg4, arg5, arg6),
        () -> fn.apply(arg1, arg2, arg3, arg4, arg5, arg6)
    );
  }

  public static <A1, A2, A3, A4, A5, R> R execute(
      FunctionIdentity fnIdentity,
      Func5<A1, A2, A3, A4, A5, R> fn,
      A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5
  ) {
    return execute(
        fnIdentity.toExecutionContext(arg1, arg2, arg3, arg4, arg5),
        () -> fn.apply(arg1, arg2, arg3, arg4, arg5)
    );
  }

  public static <A1, A2, A3, A4, R> R execute(
      FunctionIdentity fnIdentity,
      Func4<A1, A2, A3, A4, R> fn,
      A1 arg1, A2 arg2, A3 arg3, A4 arg4
  ) {
    return execute(
        fnIdentity.toExecutionContext(arg1, arg2, arg3, arg4),
        () -> fn.apply(arg1, arg2, arg3, arg4)
    );
  }

  public static <A1, A2, A3, R> R execute(
      FunctionIdentity fnIdentity,
      Func3<A1, A2, A3, R> fn,
      A1 arg1, A2 arg2, A3 arg3
  ) {
    return execute(
        fnIdentity.toExecutionContext(arg1, arg2, arg3),
        () -> fn.apply(arg1, arg2, arg3)
    );
  }

  public static <A1, A2, R> R execute(
      FunctionIdentity fnIdentity,
      Func2<A1, A2, R> fn,
      A1 arg1, A2 arg2
  ) {
    return execute(
        fnIdentity.toExecutionContext(arg1, arg2),
        () -> fn.apply(arg1, arg2)
    );
  }

  public static <A1, R> R execute(
      FunctionIdentity fnIdentity,
      Func1<A1, R> fn,
      A1 arg1
  ) {
    return execute(
        fnIdentity.toExecutionContext(arg1),
        () -> fn.apply(arg1)
    );
  }

  public static <R> R execute(FunctionIdentity fnIdentity, Func<R> fn) {
    return execute(
        fnIdentity.toExecutionContext(),
        fn::apply
    );
  }

  static <R> R execute(ExecutionContext context, ThrowingSupplier<R> resultSupplier) {
    ResultCache cache = MemoizeScope.get();
    if (cache == null) {
      try {
        return resultSupplier.get();

      } catch (Throwable ex) {
        throw new RuntimeException(ex);
      }
    }

    Object result = cache.get(context, () -> captureResultOrException(resultSupplier));

    if (result instanceof Throwable ex) {
      throw new RuntimeException(ex);
    }

    return result == ResultCache.NULL ? null : (R) result;
  }

  static <R> Object captureResultOrException(ThrowingSupplier<R> resultSupplier) {
    try {
      return resultSupplier.get();

    } catch (Throwable ex) {
      return ex;
    }
  }

  interface ThrowingSupplier<R> {
    R get() throws Throwable;
  }
}
