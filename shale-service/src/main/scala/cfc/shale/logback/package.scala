package cfc.shale

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.FileAppender
import com.typesafe.config.Config
import org.slf4j.Logger.ROOT_LOGGER_NAME
import org.slf4j.LoggerFactory.{getILoggerFactory, getLogger}

package object logback {

  def init(config: Config): Unit = {
    val log = rootLogger
    log.detachAndStopAllAppenders()
    val path = config.getString("cfc.shale.log-file")
    log.addAppender(createFileAppender(path))
  }

  def rootLogger = getLogger(ROOT_LOGGER_NAME)
    .asInstanceOf[ch.qos.logback.classic.Logger]

  def loggerContext = getILoggerFactory.asInstanceOf[LoggerContext]

  def createLogEncoder() = {
    val encoder = new PatternLayoutEncoder()
    encoder.setPattern(
      "%date{ISO8601} %-5level %logger{36} %X{akkaSource} - %msg%n")
    encoder.setContext(loggerContext)
    encoder.start()
    encoder
  }

  def createFileAppender(path: String) = {
    val appender = new FileAppender[ILoggingEvent]()
    appender.setFile(path)
    appender.setEncoder(createLogEncoder())
    appender.setContext(loggerContext)
    appender.start()
    appender
  }
}
