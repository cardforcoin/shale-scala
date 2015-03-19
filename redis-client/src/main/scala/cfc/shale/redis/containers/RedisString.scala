package cfc.shale.redis.containers

import cfc.shale.redis.commands._

case class RedisString(key: String) extends RedisContainer[String] {

  override def getCommand: RedisCommand[String] =
    RedisGetStringOption(key).map(_.getOrElse(""))

  override def setCommand(value: String): RedisCommand[Unit] =
    value match {
      case "" => RedisDelete(key)
      case x => RedisSetString(key, x)
    }
}