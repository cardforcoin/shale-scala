package cfc.shale.redis.containers

import cfc.shale.redis.commands._

class RedisBoolean(key: String) extends RedisContainer[Boolean] {

  override def get: RedisCommand[Boolean] =
    RedisGetBooleanOption(key).map(_.getOrElse(false))

  override def set(value: Boolean): RedisCommand[Unit] =
    value match {
      case false => RedisDelete(key)
      case true => RedisSetBoolean(key, true)
    }
}
