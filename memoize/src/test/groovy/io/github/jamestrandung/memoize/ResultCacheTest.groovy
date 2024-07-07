package io.github.jamestrandung.memoize

import spock.lang.Specification

class ResultCacheTest extends Specification {
    def cache = new ResultCache()

    def "test get"() {
        given:
        def context1 = new ExecutionContext(ResultCacheTest.class, "method1", null)
        def context2 = new ExecutionContext(ResultCacheTest.class, "method2", null)

        def nullSupplier = () -> null

        def count = 0
        def supplier = () -> ++count

        expect:
        cache.get(context1, nullSupplier) == ResultCache.NULL
        cache.get(context1, nullSupplier) == ResultCache.NULL

        cache.get(context2, supplier) == 1
        cache.get(context2, supplier) == 1
    }
}
