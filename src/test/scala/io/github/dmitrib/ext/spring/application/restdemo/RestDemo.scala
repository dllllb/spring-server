package io.github.dmitrib.ext.spring.application.restdemo

import java.util

import io.github.dmitrib.ext.spring.application.SpringWebApp
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Controller
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.{AnnotationConfigWebContextLoader, WebAppConfiguration}
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.bind.annotation._
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.config.annotation.{EnableWebMvc, WebMvcConfigurerAdapter}

import scala.beans.BeanProperty
import scala.collection.mutable

object City {
  def apply(name: String) = {
    val res = new City
    res.name = name
    res
  }
}

class City {
  @BeanProperty var name: String = _
}

@Configuration
@EnableWebMvc
class RestDemoContext extends WebMvcConfigurerAdapter {
  @Bean def demoController = new DemoController

  override def configureMessageConverters(converters: util.List[HttpMessageConverter[_]]) {
    converters.add(new MappingJackson2HttpMessageConverter)
  }
}

@Controller
class DemoController {

  import collection.JavaConverters._

  val cities = mutable.Map("mos" -> City("Moscow"))

  @RequestMapping(Array("/cities"))
  @ResponseBody
  def getCities = cities.values.asJava

  @RequestMapping(value = Array("/city/{id}"), method = Array(RequestMethod.PUT))
  @ResponseBody
  def addCity(@PathVariable("id") cityId: String, @RequestBody city: City) {
    cities += cityId -> city
  }

  @RequestMapping(value = Array("/city/{id}"), method = Array(RequestMethod.GET))
  @ResponseBody
  def getCity(@PathVariable("id") cityId: String) = cities(cityId)
}

@RunWith(classOf[SpringJUnit4ClassRunner])
@WebAppConfiguration
@ContextConfiguration(loader = classOf[AnnotationConfigWebContextLoader], classes = Array(classOf[RestDemoContext]))
class RestDemoTest {

  import MockMvcRequestBuilders._
  import MockMvcResultMatchers._

  @Autowired
  var context: WebApplicationContext = _

  @Test def testController() {
    val mvc = MockMvcBuilders.webAppContextSetup(context).build

    mvc.perform(
      put("/city/bar")
        .content("{\"name\":\"Barcelona\"}")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status.isOk)

    mvc.perform(get("/city/bar"))
      .andExpect(status.isOk)
      .andExpect(jsonPath("$.name").value("Barcelona"))

    mvc.perform(get("/cities"))
      .andExpect(status.isOk)
      .andExpect(jsonPath("$[?(@.name == 'Barcelona')]").exists())
      .andExpect(jsonPath("$[?(@.name == 'Moscow')]").exists())
  }
}

/**
 * @author Dmitri Babaev (dmitri.babaev@gmail.com)
 */
object RestDemo {
  def main(args: Array[String]) {
    val app = new SpringWebApp(classOf[RestDemoContext])
    app.start()
  }
}
