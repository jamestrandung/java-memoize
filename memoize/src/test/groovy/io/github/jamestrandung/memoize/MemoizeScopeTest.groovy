package io.github.jamestrandung.memoize

import spock.lang.Specification

class MemoizeScopeTest extends Specification {
    def cleanup() {
        MemoizeScope.close()
    }

    def "test reuse"() {
        given:
        def cache = new ResultCache()
        def another = new ResultCache()

        when:
        MemoizeScope.reuse(null)

        then:
        thrown(IllegalArgumentException)

        when:
        MemoizeScope.reuse(cache)

        then:
        noExceptionThrown()
        MemoizeScope.get() == cache

        when:
        MemoizeScope.reuse(another)

        then:
        noExceptionThrown()
        MemoizeScope.get() == another
    }

    def "test close"() {
        expect:
        MemoizeScope.get() == null

        when:
        MemoizeScope.initialize()

        then:
        MemoizeScope.get() != null

        when:
        MemoizeScope.close()

        then:
        MemoizeScope.get() == null
    }
}
