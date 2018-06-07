package io.github.dmitrib.ext.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Dmitri Babaev (dmitri.babaev@gmail.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableEnvironmentProperties.Context.class)
public @interface EnableEnvironmentProperties {
    @Configuration
    public static class Context {
        @Bean
        public static PropertySourcesPlaceholderConfigurer placeholderConfig() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }
}
