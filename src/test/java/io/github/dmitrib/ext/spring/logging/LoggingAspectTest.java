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
public class LoggingAspectTest {

    @Configuration
    @EnableAspectJAutoProxy
    static public class ContextCofiguration {

        @Bean
        public LoggingAspect loggingAspect() {
            return new LoggingAspect() {
                @Override
                protected Logger getLogger(Class<?> clazz) {
                    return logger();
                }
            };
        }

        @Bean
        public LoggableBean loggableBean() {
            return new LoggableBean();
        }

        @Bean
        public Logger logger() {
            Logger logger = mock(Logger.class);
            when(logger.isInfoEnabled()).thenReturn(true);
            return logger;
        }
    }

    static public class LoggableBean {
        @Logged(Level.INFO)
        public int inc(int arg) {
            return ++arg;
        }
    }

    @Autowired
    private LoggableBean logabbleBean;

    @Autowired
    private Logger logger;

    @Test
    public void testLogging() {
        logabbleBean.inc(0);
        verify(logger, times(2)).info(anyString());
        verify(logger, times(1)).isInfoEnabled();
    }
}
