package com.github.jamestrandung.memoize;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.function.Supplier;

public class ResultCache {
  /*
  Since Caffeine doesn't accept NULL as a valid value, we need a
  special object to represent NULL.
   */
  public static final Object NULL = new Object();
  /*
  Just in case we have a memory leak, actively clean up data using TTL.
   */
  private static final Duration SAFEGUARD_TTL_DURATION = Duration.ofSeconds(30);

  private final Cache<ExecutionContext, Object> cache = Caffeine.newBuilder()
      .expireAfterAccess(SAFEGUARD_TTL_DURATION)
      .build();

  public void clear() {
    this.cache.invalidateAll();
  }

  public Object get(ExecutionContext context, Supplier<Object> resultSupplier) {
    return this.cache.get(context, key -> {
      Object result = resultSupplier.get();
      return result == null ? NULL : result;
    });
  }
}
