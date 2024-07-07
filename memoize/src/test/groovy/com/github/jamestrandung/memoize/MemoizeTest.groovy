package com.github.jamestrandung.memoize

import spock.lang.Specification

class MemoizeTest extends Specification {
    def cache = Spy(new ResultCache())

    def context = new ExecutionContext(MemoizeTest.class, "test", null)
    def supplier = () -> "String"
    def failingSupplier = () -> {
        throw new IllegalAccessError()
    }

    def cleanup() {
        cache.clear()
        MemoizeScope.close()
    }

    def "test execute, MemoizeScope was NOT initialized"() {
        when: "supplier completes successfully"
        def result = Memoize.execute(context, supplier)

        then:
        0 * cache.get(_, _)

        and:
        result == "String"

        when: "supplier fails"
        Memoize.execute(context, failingSupplier)

        then:
        thrown(RuntimeException)
    }

    def "test execute, MemoizeScope was initialized, ResultCache returns an exception"() {
        given:
        MemoizeScope.reuse(cache)

        when:
        Memoize.execute(context, failingSupplier)

        then:
        1 * cache.get(context, _) >> new IllegalAccessError()

        then:
        thrown(RuntimeException)
    }

    def "test execute, MemoizeScope was initialized, ResultCache returns ResultCache.NULL"() {
        given:
        MemoizeScope.reuse(cache)

        when:
        def result = Memoize.execute(context, supplier)

        then:
        1 * cache.get(context, _) >> ResultCache.NULL

        then:
        result == null
    }

    def "test execute, MemoizeScope was initialized, ResultCache returns a value"() {
        given:
        MemoizeScope.reuse(cache)

        when:
        def result = Memoize.execute(context, supplier)

        then:
        1 * cache.get(context, _) >> "a value"

        then:
        result == "a value"
    }

    def "test captureResultOrException"() {
        expect:
        Memoize.captureResultOrException(supplier) == "String"
        Memoize.captureResultOrException(failingSupplier).class == IllegalAccessError.class
    }
}
