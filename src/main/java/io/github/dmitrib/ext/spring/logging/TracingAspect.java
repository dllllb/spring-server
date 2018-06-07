package io.github.dmitrib.ext.spring.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Babaev (dmitri.babaev@gmail.com)
 */
@Aspect
public class TracingAspect {
    private static final String DEFAULT_AFTER_PATTERN = "%s execution time: %f ms";

    @Around(value = "execution(* *(..)) && @annotation(traced)", argNames = "pjp,traced")
    public Object doParametersLogging(ProceedingJoinPoint pjp, Traced traced)
            throws Throwable {
        Logger logger = getLogger(pjp.getSignature().getDeclaringType());

        Level level = traced.value();

        if (!Level.isLoggingEnabled(logger, level)) {
            return pjp.proceed();
        }

        long beforeNanos = System.nanoTime();

        Object res = pjp.proceed();

        long executionTimeNanos = System.nanoTime() - beforeNanos;

        Level.log(logger, level, tracingLogEntry(pjp.getSignature().getName(), executionTimeNanos));

        return res;
    }

    protected Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    protected String tracingLogEntry(String name, long executionTimeNanos) {
        double millis = (double)executionTimeNanos / 1000000D;
        return String.format(DEFAULT_AFTER_PATTERN, name, millis);
    }
}
