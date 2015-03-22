package cfc.shale.redis_client.commands

import cfc.shale.redis_client.Redis

import scalaz.concurrent.Task

case class RedisDeleteKeyPrefix(keyPrefix: String) extends RedisCommand[Unit] {

  override def task(implicit redis: Redis): Task[Unit] =
    Task(redis.clients.withClient(_.evalBulk(
      """
        |for _,k in ipairs(redis.call('keys', ARGV[1])) do
        |  redis.call('del', k)
        |end
      """.stripMargin,
      Nil,
      List(redis.prefix(keyPrefix))
    )))
}
