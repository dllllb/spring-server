package io.github.dmitrib.ext.spring.application

import java.io.Writer

import org.eclipse.jetty.server.LocalConnector
import org.eclipse.jetty.testing.HttpTester
import org.junit.Assert._
import org.junit.Test
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

object SpringWebAppTest {

  @Controller
  class SimpleController {
    @RequestMapping(Array("/testHello"))
    def testHello(response: Writer) {
      response.write(
        """<html>
          <body>Hello</body>
        </html>"""
      )
    }
  }

  @Configuration
  class SimpleWebAppContext {
    @Bean def simpleController = new SimpleController
  }

  class LocalSpringWebApp(contextConfigLocation: Class[_]) extends SpringWebApp(contextConfigLocation) {
    val connector = new LocalConnector

    override protected def newConnector = connector
  }

}

/**
 * @author Dmitri Babaev (dmitri.babaev@gmail.com)
 */
class SpringWebAppTest {

  import io.github.dmitrib.ext.spring.application.SpringWebAppTest._

  @Test def testLocalSpringWebApp() {
    val app = new LocalSpringWebApp(classOf[SimpleWebAppContext])
    app.start()

    var request = new HttpTester
    request.setMethod("GET")
    request.setHeader("Host", "tester")
    request.setURI("/testHello")
    request.setVersion("HTTP/1.1")

    var response = new HttpTester
    response.parse(app.connector.getResponses(request.generate))

    assertEquals(200, response.getStatus)
    assertEquals("OK", response.getReason)
    assertTrue(response.getContent.contains("Hello"))

    app.stop()
  }
}