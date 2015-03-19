package cfc.shale.redis.containers

import cfc.shale.redis.commands._

case class RedisStringOption(key: String)
    extends RedisContainer[Option[String]] {

  override def getCommand: RedisCommand[Option[String]] =
    RedisGetStringOption(key)

  override def setCommand(value: Option[String]): RedisCommand[Unit] =
    value match {
      case Some(x) => RedisSetString(key, x)
      case None => RedisDelete(key)
    }
}
