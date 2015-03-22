package cfc.shale.redis_client.containers

import cfc.shale.redis_client.commands._

class RedisBooleanOption(key: String)
    extends RedisContainer[Option[Boolean]] {

  override def get: RedisCommand[Option[Boolean]] =
    RedisGetBooleanOption(key)

  override def set(value: Option[Boolean]): RedisCommand[Unit] =
    value match {
      case Some(x) => RedisSetBoolean(key, x)
      case None => RedisDelete(key)
    }
}
