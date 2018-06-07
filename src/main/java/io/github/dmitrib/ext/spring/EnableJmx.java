package io.github.dmitrib.ext.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.export.MBeanExporter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Dmitri Babaev (dmitri.babaev@gmail.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableJmx.Context.class)
public @interface EnableJmx {
    @Configuration
    public static class Context {
        @Bean
        public MBeanExporter mBeanExporter() {
            MBeanExporter res = new MBeanExporter();
            res.setAutodetect(true);
            return res;
        }
    }
}
