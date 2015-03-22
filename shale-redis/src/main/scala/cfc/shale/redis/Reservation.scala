package cfc.shale.redis

import cfc.shale.core.ReservationId
import org.joda.time.{Duration, Instant}

import scalaz.Monoid

case class Reservation(
  id: Option[ReservationId] = None,
  reason: Option[String] = None,
  timeout: Option[Duration] = None,
  expires: Option[Instant] = None
)

object Reservation {

  implicit val monoid = new Monoid[Reservation] {

    override def zero = Reservation()

    override def append(f1: Reservation, f2: => Reservation): Reservation =
      Reservation(
        id = f2.id orElse f1.id,
        reason = f2.reason orElse f1.reason,
        timeout = f2.timeout orElse f1.timeout,
        expires = f2.expires orElse f1.expires
      )
  }
}
