package cfc.shale.redis_client
package commands

import org.joda.time.Duration

import scalaz.concurrent.Task

case class RedisSetString(key: String, value: String, expiry: Option[Duration] = None)
    extends RedisCommand[Unit] {

  override def task(implicit redis: Redis) =
    Task(redis.clients.withClient(_.setex(redis.prefix(key), expiry, value)))
}
