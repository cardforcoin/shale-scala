package cfc.shale.redis_client.containers

import cfc.shale.redis_client.Redis
import cfc.shale.redis_client.commands.RedisCommand

import scala.collection.immutable.Map
import scalaz.concurrent.Task

case class RedisHashOption(key: String) extends RedisContainer[Option[Map[String, String]]] {

  override def get: RedisCommand[Option[Map[String, String]]] =
    new RedisCommand[Option[Map[String, String]]] {
      override def task(implicit redis: Redis) =
        Task(redis.clients.withClient(_.hgetall(redis.prefix(key))))
    }

  override def set(value: Option[Map[String, String]]): RedisCommand[Unit] =
    new RedisCommand[Unit] {
      override def task(implicit redis: Redis) =
        Task(redis.clients.withClient(_.pipeline(client => {
          client.del(redis.prefix(key))
          value.foreach(x => client.hmset(redis.prefix(key), x))
        })))
    }
}
