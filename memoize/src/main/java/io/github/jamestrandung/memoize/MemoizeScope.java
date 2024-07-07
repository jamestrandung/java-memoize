package io.github.jamestrandung.memoize;

import lombok.extern.slf4j.Slf4j;

/**
 * MemoizeScope allows us to declare the start and the end of the logic that requires memoization so that our cached results do not
 * leak into the next iteration of this logic.
 * <p>
 * For example, each endpoint in a @RestController can be used to declare MemoizeScope so that every API request would have its own
 * cache of memoized results.
 * <p>
 * Any methods marked with @KafkaListener representing a Kafka consumer logic can also be used to declare MemoizeScope so that each
 * Kafka event would have its own cache of memoized results.
 */
@Slf4j
public class MemoizeScope {
  private static final ThreadLocal<ResultCache> LOCAL_CACHE = new ThreadLocal<>();

  public static void initialize() {
    reuse(new ResultCache());
  }

  public static void reuse(ResultCache cache) {
    if (cache == null) {
      throw new IllegalArgumentException("ResultCache to reuse cannot be null");
    }

    ResultCache existing = LOCAL_CACHE.get();
    if (existing != null) {
      log.error("MemoizeScope.reuse old ResultCache was not closed properly");
    }

    LOCAL_CACHE.set(cache);
  }

  public static ResultCache get() {
    return LOCAL_CACHE.get();
  }

  public static void close() {
    ResultCache existing = LOCAL_CACHE.get();
    if (existing == null) {
      return;
    }

    LOCAL_CACHE.remove();
  }
}
