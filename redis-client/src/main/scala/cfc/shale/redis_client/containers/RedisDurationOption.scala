package cfc.shale.redis_client.containers

import org.joda.time.Duration

import scala.util.Try
import scalaz.BijectionT._
import scalaz.Scalaz._

object RedisDurationOption {

  def apply(key: String): RedisContainer[Option[Duration]] =
    RedisStringOption(key).xmapb(stringBijection)

  implicit val stringBijection: Bijection[Option[String], Option[Duration]] =
    bijection[Id, Id, Option[String], Option[Duration]](
      _.flatMap(string => Try(Duration.parse(string)).toOption),
      _.map(_.toString)
    )
}
