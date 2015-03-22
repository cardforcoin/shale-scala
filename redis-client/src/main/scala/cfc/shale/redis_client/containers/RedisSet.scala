package cfc.shale.redis_client.containers

import cfc.shale.redis_client.commands.RedisCommand

import scala.collection.immutable.Set
import scalaz.BijectionT._

trait RedisSet[A] { self =>

  def get: RedisCommand[Set[A]]

  def add(value: A): RedisCommand[Unit]

  def remove(value: A): RedisCommand[Unit]

  def biject[B](implicit bijection: Bijection[A, B]): RedisSet[B] =
    new RedisSet[B] {
      override def get = self.get.map(_.map(bijection.to))
      override def add(value: B) = self.add(bijection.from(value))
      override def remove(value: B) = self.remove(bijection.from(value))
    }
}
