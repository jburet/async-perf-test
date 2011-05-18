package fr.jbu.asyncperf.util

import org.slf4j.{Logger, LoggerFactory}


trait Slf4jLogger {

  private val logger = LoggerFactory.getLogger(getClass)

  def isTraceEnabled = logger.isTraceEnabled

  def trace(msg: => AnyRef) = if (isTraceEnabled) logger.trace(String.valueOf(msg))

  def trace(msg: => AnyRef, t: => Throwable) = if (isTraceEnabled) logger.trace(String.valueOf(msg), t)

  def assertLog(assertion: Boolean, msg: => String) = if (assertion) info(msg)

  def isDebugEnabled = logger.isDebugEnabled

  def debug(msg: => AnyRef) = if (isDebugEnabled) logger.debug(String.valueOf(msg))

  def debug(msg: => AnyRef, t: => Throwable) = if (isDebugEnabled) logger.debug(String.valueOf(msg), t)

  def isErrorEnabled = logger.isErrorEnabled

  def error(msg: => AnyRef) = if (isErrorEnabled) logger.error(String.valueOf(msg))

  def error(msg: => AnyRef, t: => Throwable) = if (isErrorEnabled) logger.error(String.valueOf(msg), t)

  def fatal(msg: AnyRef) = error(msg)

  def fatal(msg: AnyRef, t: Throwable) = error(msg, t)

  //   def level_=(level: LiftLogLevels.Value) = logger.setLevel(liftToLog4J(level) )
  def name = logger.getName

  def isInfoEnabled = logger.isInfoEnabled

  def info(msg: => AnyRef) = if (isInfoEnabled) logger.info(String.valueOf(msg))

  def info(msg: => AnyRef, t: => Throwable) = if (isInfoEnabled) logger.info(String.valueOf(msg), t)

  def isWarnEnabled = logger.isWarnEnabled

  def warn(msg: => AnyRef) = if (isWarnEnabled) logger.warn(String.valueOf(msg))

  def warn(msg: => AnyRef, t: => Throwable) = if (isWarnEnabled) logger.warn(String.valueOf(msg), t)
}