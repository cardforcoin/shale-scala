package cfc.shale.redis.containers

import cfc.shale.redis.commands.RedisCommand

import scalaz.BijectionT.Bijection

trait RedisContainer[A] { self =>

  def get: RedisCommand[A]
  
  def set(value: A): RedisCommand[Unit]

  def biject[B](implicit bijection: Bijection[A, B]): RedisContainer[B] =
    new RedisContainer[B] {

      override def get = self.get.map(bijection.to)

      override def set(value: B) = self.set(bijection.from(value))
    }
}
