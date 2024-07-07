package io.github.jamestrandung.memoize;

import io.github.jamestrandung.memoize.Memoized.MemoizeAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class MemoizeConfiguration {
  @Bean(name = {"io.github.jamestrandung.internalMemoizeAspect"})
  public MemoizeAspect memoizeAspect() {
    return new MemoizeAspect();
  }
}
