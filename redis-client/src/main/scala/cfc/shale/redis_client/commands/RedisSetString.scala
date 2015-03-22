package cfc.shale.redis_client.commands

import cfc.shale.redis_client.Redis

import scalaz.concurrent.Task

case class RedisSetString(key: String, value: String)
    extends RedisCommand[Unit] {

  override def task(implicit redis: Redis) =
    Task(redis.clients.withClient(_.set(redis.prefix(key), value)))
}
