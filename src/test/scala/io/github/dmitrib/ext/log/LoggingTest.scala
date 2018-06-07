package io.github.dmitrib.ext.log

import org.junit.Test

class ClassWithLog extends Logging {
  def doWork(token: String) {
    log.info(s"doing work, token:$token")
  }
}

/**
 * @author Dmitri Babaev (dmitri.babaev@gmail.com)
 */
class LoggingTest {
  @Test def testLoggingAspect() {
    new ClassWithLog().doWork("1")
  }
}
