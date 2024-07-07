package com.github.jamestrandung.samples;

import com.github.jamestrandung.memoize.EnableMemoize;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableMemoize
@SpringBootConfiguration
@ComponentScan("com.github.jamestrandung.samples")
public class WithAnnotationSpringApp {
  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(WithAnnotationSpringApp.class, args);
  }
}
