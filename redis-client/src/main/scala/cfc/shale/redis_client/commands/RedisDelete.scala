package cfc.shale.redis_client.commands

import cfc.shale.redis_client.Redis

import scalaz.concurrent.Task

case class RedisDelete(key: String) extends RedisCommand[Unit] {

  override def task(implicit redis: Redis): Task[Unit] =
    Task(redis.clients.withClient(_.del(redis.prefix(key))))
}
