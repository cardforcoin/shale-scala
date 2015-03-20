package cfc.shale.redis.containers

import cfc.shale.redis.commands.RedisCommand

import scala.language.higherKinds
import scalaz.BijectionT._
import scalaz.Scalaz._
import scalaz._

trait RedisContainer[A] { self =>

  def get: RedisCommand[A]
  
  def set(value: A): RedisCommand[Unit]

  def biject[B](implicit bijection: Bijection[A, B]): RedisContainer[B] =
    new RedisContainer[B] {

      override def get = self.get.map(bijection.to)

      override def set(value: B) = self.set(bijection.from(value))
    }
}

object RedisContainer {

  // https://stackoverflow.com/questions/19455470/lifting-a-bijection-into-a-functor
  implicit class BijectionLifter[A, B](val bij: A <@> B) extends AnyVal {
    def liftInto[F[_]: Functor]: F[A] <@> F[B] = bijection[Id, Id, F[A], F[B]](
      _ map bij.to,
      _ map bij.from
    )
  }
}
