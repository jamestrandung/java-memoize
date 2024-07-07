package com.github.jamestrandung.samples;

import com.github.jamestrandung.memoize.Functions.FunctionIdentity;
import com.github.jamestrandung.memoize.LocalResultCache;
import com.github.jamestrandung.memoize.Memoize;
import com.github.jamestrandung.memoize.ResultCache;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MultiThreadingSample {
  public static void main(String[] args) {
    restControllerMethod();
  }

  //  @GetMapping("/imaginary-rest-endpoint-with-threads")
  static void restControllerMethod() {
    try {
      LocalResultCache.initialize();

      callServiceImplementation();

    } finally {
      LocalResultCache.close();
    }
  }

  static void callServiceImplementation() {
    Memoize.execute(
        FunctionIdentity.of(MultiThreadingSample.class, "executeInParent"),
        MultiThreadingSample::executeInParent
    );

    spawnThreads();
  }

  static void spawnThreads() {
    ResultCache cache = LocalResultCache.get();

    List<CompletableFuture<Void>> futures = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try {
              // Reuse all results computed by parent and other threads
              LocalResultCache.reuse(cache);

              return performThreadLogic();

            } finally {
              LocalResultCache.close();
            }
          })
          .thenAccept(result -> {
            log.info("[{}] Final result: {}", Thread.currentThread().getName(), result);
          });

      futures.add(future);
    }

    sleepSilently(1000L);

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
  }

  static String performThreadLogic() {
    String parentResult = Memoize.execute(
        FunctionIdentity.of(MultiThreadingSample.class, "executeInParent"),
        MultiThreadingSample::executeInParent
    );

    String childResult = Memoize.execute(
        FunctionIdentity.of(MultiThreadingSample.class, "executeInChildThread"),
        MultiThreadingSample::executeInChildThread
    );

    return parentResult + "-" + childResult;
  }

  static String executeInParent() {
    log.info("Get printed only once, executed by the parent thread");
    return "parent";
  }

  static String executeInChildThread() {
    log.info("Get printed only once, executed by ONE of the child threads");
    return "child";
  }

  static void sleepSilently(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
