package cfc.shale.sessions

import scalaz.BijectionT._
import scalaz.Scalaz._

/** An arbitrary value identifying a session. Note that this id is assigned by Shale,
  * and it is different from the session id assigned to a Selenium webdriver. */
case class ShaleSessionId(value: String)

object ShaleSessionId {

  implicit val stringBijection: Bijection[String, ShaleSessionId] =
    bijection[Id, Id, String, ShaleSessionId](ShaleSessionId(_), _.value)
}
