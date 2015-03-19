package cfc.shale.redis.commands

import cfc.shale.redis.Redis

import scalaz.concurrent.Task

case class RedisGetStringOption(key: String)
    extends RedisCommand[Option[String]] {

  override def task(implicit redis: Redis): Task[Option[String]] =
    Task(redis.clients.withClient(_.get(redis.prefix(key))))
}
