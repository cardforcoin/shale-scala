package cfc.shale.sessions

import scalaz.BijectionT._
import scalaz.Scalaz._

/** An arbitrary value identifying a session. Note that this id is assigned by Shale,
  * and it is different from the session id assigned to a Selenium webdriver. */
case class SessionId(value: String)

object SessionId {

  implicit val stringBijection: Bijection[String, SessionId] =
    bijection[Id, Id, String, SessionId](SessionId(_), _.value)
}
