package cfc.shale.core

import scalaz.BijectionT._
import scalaz.Scalaz._

case class ReservationId(value: String)

object ReservationId {

  implicit val stringBijection: Bijection[String, ReservationId] =
    bijection[Id, Id, String, ReservationId](ReservationId(_), _.value)
}
