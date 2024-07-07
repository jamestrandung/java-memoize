package io.github.jamestrandung.memoize

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.Signature
import spock.lang.Specification

class MemoizeAspectTest extends Specification {
    def aspect = new Memoized.MemoizeAspect()

    def jointPoint = Mock(ProceedingJoinPoint) {
        getSignature() >> Stub(Signature) {
            getDeclaringType() >> MemoizeAspectTest.class
            getName() >> "test"
        }
        getArgs() >> null
    }

    def setup() {
        SpyStatic(Memoize)
    }

    def "test memoize"() {
        given:
        def cachedResult = new Object()

        when:
        def result = aspect.memoize(jointPoint)

        then:
        1 * Memoize.execute(new ExecutionContext(MemoizeAspectTest.class, "test", null), _) >> cachedResult

        and:
        result == cachedResult
    }
}
