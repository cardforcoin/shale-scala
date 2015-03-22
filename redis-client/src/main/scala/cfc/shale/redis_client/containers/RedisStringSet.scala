package cfc.shale.redis_client
package containers

import commands._

case class RedisStringSet(key: String) extends RedisSet[String] {

  override def get = RedisGetStringSet(key)

  override def remove(value: String): RedisCommand[Unit] =
    RedisRemoveFromStringSet(key, value)

  override def add(value: String): RedisCommand[Unit] =
    RedisAddToStringSet(key, value)
}
