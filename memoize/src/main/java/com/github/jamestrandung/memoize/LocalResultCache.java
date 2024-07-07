package com.github.jamestrandung.memoize;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalResultCache {
  private static final ThreadLocal<ResultCache> LOCAL_CACHE = new ThreadLocal<>();

  // TODO: support use case?

  public static void initialize() {
    reuse(new ResultCache());
  }

  public static void reuse(ResultCache cache) {
    if (cache == null) {
      throw new IllegalArgumentException("ResultCache to reuse cannot be null");
    }

    ResultCache existing = LOCAL_CACHE.get();
    if (existing != null) {
      log.error("LocalResultCache.reuse old ResultCache was not closed properly");
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
