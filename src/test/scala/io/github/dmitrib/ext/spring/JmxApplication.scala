package io.github.dmitrib.ext.spring

import javax.management.MXBean

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.{AnnotationConfigApplicationContext, Bean, Configuration}

/**
 * @author Dmitri Babaev (dmitri.babaev@gmail.com)
 */
object JmxApplication {

  @MXBean
  trait Management {
    def getName: String

    def operation()
  }

  class TestBean(name: String) extends Management {
    protected val log = LoggerFactory.getLogger(this.getClass)

    def getName = name

    def operation() {
      log.info("managed operation is called")
    }
  }

  @Configuration
  @EnableJmx
  class Context {
    @Bean(name = Array("org.ext.scala.snippet:type=Management"))
    def testBean = new TestBean("test")
  }

  def main(args: Array[String]) {
    new AnnotationConfigApplicationContext(classOf[Context])
    while (true) {
      Thread.sleep(100)
    }
  }
}