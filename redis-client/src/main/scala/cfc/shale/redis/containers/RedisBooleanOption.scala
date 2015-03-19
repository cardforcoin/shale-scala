package cfc.shale.redis.containers

import cfc.shale.redis.commands._

class RedisBooleanOption(key: String)
    extends RedisContainer[Option[Boolean]] {

  override def getCommand: RedisCommand[Option[Boolean]] =
    RedisGetBooleanOption(key)

  override def setCommand(value: Option[Boolean]): RedisCommand[Unit] =
    value match {
      case Some(x) => RedisSetBoolean(key, x)
      case None => RedisDelete(key)
    }
}
