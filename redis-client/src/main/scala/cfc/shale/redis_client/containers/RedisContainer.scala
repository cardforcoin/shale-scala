package cfc.shale.redis_client.containers

import cfc.shale.redis_client.commands.RedisCommand

import scala.language.higherKinds
import scalaz._

trait RedisContainer[A] { self =>

  def get: RedisCommand[A]

  def set(value: A): RedisCommand[Unit]
}

object RedisContainer {

  implicit val RedisContainerInvariantFunctor = new InvariantFunctor[RedisContainer] {
    override def xmap[A, B](ma: RedisContainer[A], f: (A) => B, g: (B) => A): RedisContainer[B] =
      new RedisContainer[B] {
        override def get = ma.get.map(f)
        override def set(value: B) = ma.set(g(value))
      }
  }
}
