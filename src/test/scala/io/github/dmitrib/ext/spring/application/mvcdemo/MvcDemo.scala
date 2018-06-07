package io.github.dmitrib.ext.spring.application.mvcdemo

import io.github.dmitrib.ext.spring.application.SpringWebApp
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.stereotype.Controller
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.{AnnotationConfigWebContextLoader, WebAppConfiguration}
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.thymeleaf.spring3.SpringTemplateEngine
import org.thymeleaf.spring3.view.ThymeleafViewResolver
import org.thymeleaf.templateresolver.ServletContextTemplateResolver

import scala.beans.BeanProperty

case class City(@BeanProperty name: String)

@Configuration
@EnableWebMvc
class MvcDemoContext {
  @Bean def templateResolver = {
    val res = new ServletContextTemplateResolver
    res.setPrefix("/io/github/dmitrib/ext/spring/application/mvcdemo/")
    res.setSuffix(".html")
    res
  }

  @Bean def templateEngine = {
    val res = new SpringTemplateEngine
    res.setTemplateResolver(templateResolver)
    res
  }

  @Bean def viewResolver = {
    val res = new ThymeleafViewResolver
    res.setTemplateEngine(templateEngine)
    res
  }

  @Bean def demoController = new DemoController
}

@Controller
class DemoController {

  import collection.JavaConverters._

  @ModelAttribute("cities")
  def cities = List(City("Moscow"), City("Berlin"), City("London")).asJava

  @RequestMapping(Array("/demo")) def demo = "demo"
}

@RunWith(classOf[SpringJUnit4ClassRunner])
@WebAppConfiguration("classpath:")
@ContextConfiguration(loader = classOf[AnnotationConfigWebContextLoader], classes = Array(classOf[MvcDemoContext]))
class MvcDemoTest {

  import MockMvcRequestBuilders._
  import MockMvcResultMatchers._

  @Autowired
  var context: WebApplicationContext = _

  @Test def testController() {
    val mvc = MockMvcBuilders.webAppContextSetup(context).build

    mvc.perform(get("/demo"))
      .andExpect(status.isOk)
      .andExpect(model.attributeExists("cities"))
      .andExpect(view.name("demo"))
      .andExpect(xpath("/html/body/ul/li/span[text() = 'Moscow']").exists())
  }
}

/**
 * @author Dmitri Babaev (dmitri.babaev@gmail.com)
 */
object MvcDemo {
  def main(args: Array[String]) {
    val app = new SpringWebApp(classOf[MvcDemoContext])
    app.start()
  }
}
