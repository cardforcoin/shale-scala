package cfc.shale.core

import scalaz.BijectionT._
import scalaz.Scalaz._

case class NodeAddress(uri: String)

object NodeAddress {

  implicit val stringBijection: Bijection[String, NodeAddress] =
    bijection[Id, Id, String, NodeAddress](NodeAddress(_), _.uri)
}
