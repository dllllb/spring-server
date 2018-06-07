package io.github.dmitrib.ext.spring.logging;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Dmitri Babaev (dmitri.babaev@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class TracingAspectTest {

    @Configuration
    @EnableAspectJAutoProxy
    static public class ContextCofiguration {

        @Bean
        public TracingAspect tracingAspect() {
            return new TracingAspect() {
                @Override
                protected Logger getLogger(Class<?> clazz) {
                    return logger();
                }
            };
        }

        @Bean
        public TracebleBean loggableBean() {
            return new TracebleBean();
        }

        @Bean
        public Logger logger() {
            Logger logger = mock(Logger.class);
            when(logger.isInfoEnabled()).thenReturn(true);
            return logger;
        }
    }

    static public class TracebleBean {
        @Traced(Level.INFO)
        public int inc(int arg) {
            return arg++;
        }
    }

    @Autowired
    private TracebleBean tracebleBean;

    @Autowired
    private Logger logger;

    @Test
    public void testTracing() {
        tracebleBean.inc(0);
        verify(logger, times(1)).info(anyString());
        verify(logger, times(1)).isInfoEnabled();
    }
}
