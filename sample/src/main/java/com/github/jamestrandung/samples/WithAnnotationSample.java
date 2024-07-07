package com.github.jamestrandung.samples;

import com.github.jamestrandung.memoize.LocalResultCache;
import com.github.jamestrandung.memoize.Memoized;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WithAnnotationSample {
  @Autowired
  MemoizedType memoizedType;
  @Autowired
  @Qualifier("rawType")
  RawType rawType;
  @Autowired
  AnotherMemoizedType anotherMemoizedType;
  @Autowired
  TypeWithMemoizedMethod typeWithMemoizedMethod;

  //  @KafkaListener(
  //      topics="SOME_TOPIC",
  //      groupId="CONSUMER_GROUP_ID"
  //  )
  @PreDestroy
  public void kafkaListenerMethod() {
    try {
      LocalResultCache.initialize();

      this.callServiceImplementation();

    } finally {
      LocalResultCache.close();
    }
  }

  void callServiceImplementation() {
    String result1 = this.memoizedType.doSomething("test");
    String result2 = this.memoizedType.doSomething("test");
    String result3 = this.memoizedType.doSomething("test");

    log.info("memoizedType results: {}, {}, {}", result1, result2, result3);
    System.out.println();

    String result4 = this.rawType.doSomething("RAW-TEST");
    String result5 = this.rawType.doSomething("RAW-TEST");
    String result6 = this.rawType.doSomething("RAW-TEST");

    log.info("rawType results: {}, {}, {}", result4, result5, result6);
    System.out.println();

    String result7 = this.anotherMemoizedType.doSomething("ANOTHER-TEST");
    String result8 = this.anotherMemoizedType.doSomething("ANOTHER-TEST");
    String result9 = this.anotherMemoizedType.doSomething("ANOTHER-TEST");

    log.info("anotherMemoizedType results: {}, {}, {}", result7, result8, result9);
    System.out.println();

    Integer result10 = this.typeWithMemoizedMethod.increment(1);
    Integer result11 = this.typeWithMemoizedMethod.increment(1);
    Integer result12 = this.typeWithMemoizedMethod.increment(2);

    log.info("typeWithMemoizedMethod results: {}, {}, {}", result10, result11, result12);
    System.out.println();
  }

  @Memoized
  @Component
  public static class MemoizedType {
    public String doSomething(String text) {
      log.info("Get printed only once by MemoizedType");
      return StringUtils.toRootUpperCase(text);
    }
  }

  @Memoized
  @Component
  public static class AnotherMemoizedType extends RawType {
  }

  @Component("rawType")
  public static class RawType {
    public String doSomething(String text) {
      log.info("Get printed 3 times by RawType and only once by AnotherMemoizedType");
      return StringUtils.toRootLowerCase(text);
    }
  }

  @Component
  public static class TypeWithMemoizedMethod {
    @Memoized
    public Integer increment(int number) {
      log.info("Get printed twice for 1 and 2 by TypeWithMemoizedMethod, input: {}", number);
      return number + 1;
    }
  }
}
