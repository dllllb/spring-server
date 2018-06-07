package io.github.dmitrib.ext.spring.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author Dmitri Babaev (dmitri.babaev@gmail.com)
 */
@Aspect
public class LoggingAspect {
    private static final String DEFAULT_BEFORE_PATTERN = "Executing %s, arguments: %s";
    private static final String DEFAULT_AFTER_PATTERN = "%s execution result: %s";

    @Around(value = "execution(* *(..)) && @annotation(logged)", argNames = "pjp,logged")
    public Object doParametersLogging(ProceedingJoinPoint pjp, Logged logged)
            throws Throwable {
        Logger logger = getLogger(pjp.getSignature().getDeclaringType());

        Level level = logged.value();

        if (!Level.isLoggingEnabled(logger, level)) {
            return pjp.proceed();
        }

        Level.log(logger, level, beforeLogEntry(pjp.getSignature().getName(), pjp.getArgs()));

        Object res = pjp.proceed();

        Level.log(logger, level, afterLogEntry(pjp.getSignature().getName(), res));

        return res;
    }

    protected Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    protected String beforeLogEntry(String name, Object[] args) {
        return String.format(DEFAULT_BEFORE_PATTERN, name, argsAsString(args));
    }

    protected String afterLogEntry(String name, Object res) {
        return String.format(DEFAULT_AFTER_PATTERN, name, toString(res));
    }

    private static String toString(Object obj) {
        if (obj == null) {
            return "null";
        } else if (obj instanceof Object[]) {
            return Arrays.toString((Object[]) obj);
        } else {
            return obj.toString();
        }
    }

    protected String argsAsString(Object[] args) {
        if (args == null || args.length == 0) {
            return "[NONE]";
        }

        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            if (arg == null) {
                sb.append("null, ");
            } else {
                sb.append(arg.getClass().getSimpleName())
                        .append(" : ")
                        .append(toString(arg))
                        .append(", ");
            }
        }
        sb.delete(sb.length() - 2, sb.length() - 1); // remove trailing comma

        return sb.toString();
    }
}
