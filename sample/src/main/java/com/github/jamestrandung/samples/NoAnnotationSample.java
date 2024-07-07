package com.github.jamestrandung.samples;

import com.github.jamestrandung.memoize.Functions.FunctionIdentity;
import com.github.jamestrandung.memoize.Memoize;
import com.github.jamestrandung.memoize.MemoizeScope;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoAnnotationSample {
  public static void main(String[] args) {
    restControllerMethod();
  }

  //  @GetMapping("/imaginary-rest-endpoint")
  static void restControllerMethod() {
    try {
      MemoizeScope.initialize();

      callServiceImplementation();

    } finally {
      MemoizeScope.close();
    }
  }

  static void callServiceImplementation() {
    String result1 = Memoize.execute(
        FunctionIdentity.of(NoAnnotationSample.class, "noArguments"),
        NoAnnotationSample::noArguments
    );

    String result2 = Memoize.execute(
        FunctionIdentity.of(NoAnnotationSample.class, "noArguments"),
        NoAnnotationSample::noArguments
    );

    String result3 = Memoize.execute(
        FunctionIdentity.of(NoAnnotationSample.class, "noArguments"),
        NoAnnotationSample::noArguments
    );

    log.info("noArguments results: {}, {}, {}", result1, result2, result3);
    System.out.println();

    Integer result4 = Memoize.execute(
        FunctionIdentity.of(NoAnnotationSample.class, "withArgument"),
        NoAnnotationSample::withArgument, 1
    );

    Integer result5 = Memoize.execute(
        FunctionIdentity.of(NoAnnotationSample.class, "withArgument"),
        NoAnnotationSample::withArgument, 1
    );

    Integer result6 = Memoize.execute(
        FunctionIdentity.of(NoAnnotationSample.class, "withArgument"),
        NoAnnotationSample::withArgument, 2
    );

    log.info("withArgument results: {}, {}, {}", result4, result5, result6);
    System.out.println();
  }

  static String noArguments() {
    log.info("Get printed only once");
    return "String";
  }

  static Integer withArgument(int number) {
    log.info("Get printed twice for 1 and 2, input: {}", number);
    return number + 1;
  }
}
