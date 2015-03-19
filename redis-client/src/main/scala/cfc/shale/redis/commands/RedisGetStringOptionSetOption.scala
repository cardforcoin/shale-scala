package cfc.shale.redis.commands

import cfc.shale.redis.Redis

import scala.collection.immutable.Set
import scalaz.concurrent.Task

case class RedisGetStringOptionSetOption(key: String)
    extends RedisCommand[Option[Set[Option[String]]]] {

  override def task(implicit redis: Redis) =
    Task(redis.clients.withClient(_.smembers(redis.prefix(key))))
}
