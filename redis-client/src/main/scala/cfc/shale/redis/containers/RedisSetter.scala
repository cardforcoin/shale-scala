package cfc.shale.redis.containers

import cfc.shale.redis.Redis
import cfc.shale.redis.commands.RedisCommand

import scalaz.concurrent.Task

trait RedisSetter[-A] {

  def setCommand(value: A): RedisCommand[Unit]

  def set(value: A)(implicit redis: Redis): Task[Unit] =
    setCommand(value).task
}

object RedisSetter {

  def apply[A](command: RedisCommand[Unit]) = new RedisSetter[A] {
    override def setCommand(value: A): RedisCommand[Unit] = command
  }
}
