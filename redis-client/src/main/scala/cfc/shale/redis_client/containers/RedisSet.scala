package cfc.shale.redis_client.containers

import cfc.shale.redis_client.commands.RedisCommand

import scala.collection.immutable.Set
import scalaz._

trait RedisSet[A] { self =>

  def get: RedisCommand[Set[A]]

  def add(value: A): RedisCommand[Unit]

  def remove(value: A): RedisCommand[Unit]
}

object RedisSet {

  implicit val RedisSetInvariantFunctor = new InvariantFunctor[RedisSet] {

    override def xmap[A, B](ma: RedisSet[A], f: (A) => B, g: (B) => A): RedisSet[B] =
      new RedisSet[B] {
        override def get = ma.get.map(_.map(f))
        override def add(value: B) = ma.add(g(value))
        override def remove(value: B) = ma.remove(g(value))
      }
  }
}
