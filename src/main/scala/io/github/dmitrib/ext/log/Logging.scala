package io.github.dmitrib.ext.log

import org.slf4j.LoggerFactory
import org.slf4j.Logger

/**
 * @author Dmitri Babaev (dmitri.babaev@gmail.com)
 */
trait Logging {
  protected implicit val log = new RichLogger(LoggerFactory.getLogger(this.getClass))
}

class RichLogger(val logger: Logger) {
  def info(message: String) {
    if (logger.isInfoEnabled)
      logger.info(message)
  }

  def info(throwable: Throwable, message: => String) {
    if (logger.isInfoEnabled)
      logger.info(message, throwable)
  }

  def warn(message: => String) {
    if (logger.isWarnEnabled)
      logger.warn(message)
  }

  def warn(throwable: Throwable, message: => String) {
    if (logger.isWarnEnabled)
      logger.warn(message, throwable)
  }

  def error(message: => String) {
    if (logger.isErrorEnabled)
      logger.error(message)
  }

  def error(throwable: Throwable, message: => String) {
    if (logger.isErrorEnabled)
      logger.error(message, throwable)
  }

  def debug(message: => String) {
    if (logger.isDebugEnabled)
      logger.debug(message)
  }

  def debug(throwable: Throwable, message: => String) {
    if (logger.isDebugEnabled)
      logger.debug(message, throwable)
  }

  def trace(message: => String) {
    if (logger.isTraceEnabled)
      logger.trace(message)
  }

  def trace(throwable: Throwable, message: => String) {
    if (logger.isTraceEnabled)
      logger.trace(message, throwable)
  }
}

object RichLogger {
  implicit def richLoggerToLogger(logger: RichLogger): Logger = logger.logger
}
