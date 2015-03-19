package cfc.shale.sessions

import scalaz.BijectionT._
import scalaz.Scalaz._

case class SessionId(value: String)

object SessionId {

  implicit val stringBijection: Bijection[String, SessionId] =
    bijection[Id, Id, String, SessionId](SessionId(_), _.value)
}
