package cfc.shale.redis_client.commands

import cfc.shale.redis_client.Redis

import scalaz.concurrent.Task

case class RedisAddToStringSet(key: String, value: String) extends RedisCommand[Unit] {

  override def task(implicit redis: Redis): Task[Unit] =
    Task(redis.clients.withClient(_.sadd(redis.prefix(key), value)))
}
