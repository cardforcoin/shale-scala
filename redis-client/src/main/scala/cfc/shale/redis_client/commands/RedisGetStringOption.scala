package cfc.shale.redis_client.commands

import cfc.shale.redis_client.Redis

import scalaz.concurrent.Task

case class RedisGetStringOption(key: String)
    extends RedisCommand[Option[String]] {

  override def task(implicit redis: Redis): Task[Option[String]] =
    Task(redis.clients.withClient(_.get(redis.prefix(key))))
}
