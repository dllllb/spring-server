package io.github.dmitrib.ext.spring.application

import org.eclipse.jetty.server.nio.SelectChannelConnector
import org.eclipse.jetty.server.{Connector, Server}
import org.eclipse.jetty.servlet.{ServletContextHandler, ServletHolder}
import org.eclipse.jetty.util.resource.Resource
import org.springframework.web.servlet.DispatcherServlet

/**
 * @author Dmitri Babaev (dmitri.babaev@gmail.com)
 */
class SpringWebApp(val contextConfigLocation: Class[_], address: String = "0.0.0.0", port: Int = 8080) {
  private var server: Server = _

  def doMain() {
    start()
    server.join()
  }

  def start() {
    server = new Server()

    val context = new ServletContextHandler(ServletContextHandler.SESSIONS)
    context.setContextPath("/")
    context.setBaseResource(getRootResource)

    val holder = new ServletHolder(new DispatcherServlet)
    getContextConfigurationClass.foreach(holder.setInitParameter("contextClass", _))
    holder.setInitParameter("contextConfigLocation", contextConfigLocation.toString)
    context.addServlet(holder, "/")

    val connector = newConnector

    server.addConnector(connector)
    server.setHandler(context)
    server.setStopAtShutdown(true)

    server.start()
  }

  def stop() {
    if (server != null) {
      server.stop()
      server.join()
    }
  }

  protected def getContextConfigurationClass = {
    Some("org.springframework.web.context.support.AnnotationConfigWebApplicationContext")
  }

  protected def getRootResource = Resource.newClassPathResource("/")

  protected def newConnector: Connector = {
    val httpConnector = new SelectChannelConnector()
    httpConnector.setHost(address)
    httpConnector.setPort(port)
    httpConnector
  }
}