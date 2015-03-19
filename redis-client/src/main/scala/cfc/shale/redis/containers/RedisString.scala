package cfc.shale.redis.containers

import cfc.shale.redis.commands._

case class RedisString(key: String) extends RedisContainer[String] {

  override def get: RedisCommand[String] =
    RedisGetStringOption(key).map(_.getOrElse(""))

  override def set(value: String): RedisCommand[Unit] =
    value match {
      case "" => RedisDelete(key)
      case x => RedisSetString(key, x)
    }
}
