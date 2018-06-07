package io.github.dmitrib.ext.spring

import org.junit.{Assert, Test}
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.{AnnotationConfigApplicationContext, Bean, Configuration}
import org.springframework.core.env.MapPropertySource

object PropertiesApplicationContextTest {

  case class TestBean(name: String)

  @Configuration
  @EnableEnvironmentProperties
  class ContextCofiguration {
    @Bean def bean(@Value("${spring.test.name:none}") name: String) = new TestBean(name)
  }

}

/**
 * @author Dmitri Babaev (dmitri.babaev@gmail.com)
 */
class PropertiesApplicationContextTest {

  import io.github.dmitrib.ext.spring.PropertiesApplicationContextTest._

import scala.collection.JavaConverters._

  @Test def testPropertyInjection() {
    var context = new AnnotationConfigApplicationContext
    var name = "qwerty"
    var props = Map[String, Object]("spring.test.name" -> name)
    context.getEnvironment.getPropertySources.addLast(new MapPropertySource("custom_source", props.asJava))
    context.register(classOf[ContextCofiguration])
    context.refresh()
    Assert.assertEquals(name, context.getBean(classOf[TestBean]).name)
  }
}