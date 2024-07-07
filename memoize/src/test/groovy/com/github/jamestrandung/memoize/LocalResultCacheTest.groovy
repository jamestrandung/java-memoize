package com.github.jamestrandung.memoize

import spock.lang.Specification

class LocalResultCacheTest extends Specification {
    def cleanup() {
        LocalResultCache.close()
    }

    def "test reuse"() {
        given:
        def cache = new ResultCache()
        def another = new ResultCache()

        when:
        LocalResultCache.reuse(null)

        then:
        thrown(IllegalArgumentException)

        when:
        LocalResultCache.reuse(cache)

        then:
        noExceptionThrown()
        LocalResultCache.get() == cache

        when:
        LocalResultCache.reuse(another)

        then:
        noExceptionThrown()
        LocalResultCache.get() == another
    }

    def "test close"() {
        expect:
        LocalResultCache.get() == null

        when:
        LocalResultCache.initialize()

        then:
        LocalResultCache.get() != null

        when:
        LocalResultCache.close()

        then:
        LocalResultCache.get() == null
    }
}
