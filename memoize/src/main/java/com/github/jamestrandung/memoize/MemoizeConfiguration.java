package com.github.jamestrandung.memoize;

import com.github.jamestrandung.memoize.Memoized.MemoizeAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class MemoizeConfiguration {
  @Bean(name = {"com.github.jamestrandung.internalMemoizeAspect"})
  public MemoizeAspect memoizeAspect() {
    return new MemoizeAspect();
  }
}
