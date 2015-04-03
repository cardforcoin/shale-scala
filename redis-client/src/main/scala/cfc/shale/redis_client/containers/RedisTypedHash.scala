package cfc.shale.redis_client.containers

import RedisTypedHash._
import cfc.shale.redis_client.Redis
import cfc.shale.redis_client.commands.RedisCommand

import scalaz._, Scalaz._
import scalaz.concurrent.Task
import scalaz.BijectionT._

case class RedisTypedHash[A : Monoid](key: String, fields: Seq[Field[A, Any]])
    extends RedisContainer[Option[A]] {

  override def get: RedisCommand[Option[A]] =
    RedisCommand(redis =>
      redis.clients.withClient(client =>
        client.hgetall(redis.prefix(key)).map(x =>
          fields.flatMap(f => x.get(f.key).map(f.stringBijection.to))
            .foldLeft(implicitly[Monoid[A]].zero)(implicitly[Monoid[A]].append)
        )
      ))

  override def set(value: Option[A]): RedisCommand[Unit] =
    RedisCommand(redis =

}

object RedisTypedHash {

  case class Field[A, B](key: String, monoid: Monoid[A], stringBijection: Bijection[String, A])
}
