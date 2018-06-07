package io.github.dmitrib.ext.spring.application.hessiandemo

import com.caucho.hessian.client.HessianProxyFactory
import io.github.dmitrib.ext.spring.application.SpringWebApp
import org.junit.Assert
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.remoting.caucho.HessianServiceExporter

trait CalcService {
  def sum(a: Int, b: Int): Int
}

class CalcServiceImpl extends CalcService {
  def sum(a: Int, b: Int) = a + b
}

@Configuration
class HessianDemoContext {
  @Bean(name = Array("/calc-exporter")) def exporter = {
    val res = new HessianServiceExporter
    res.setService(calcService)
    res.setServiceInterface(classOf[CalcService])
    res
  }

  @Bean def calcService = new CalcServiceImpl
}

/**
 * @author Dmitri Babaev (dmitri.babaev@gmail.com)
 */
object HessianDemo {
  def main(args: Array[String]) {
    val app = new SpringWebApp(classOf[HessianDemoContext])
    app.start()
  }
}

object HessianDemoClient {
  def main(args: Array[String]) {
    val factory = new HessianProxyFactory
    val service = factory.create(classOf[CalcService], "http://localhost:8080/calc-exporter")
      .asInstanceOf[CalcService]
    val sum = service.sum(1, 2)
    Assert.assertEquals(3, sum)
  }
}