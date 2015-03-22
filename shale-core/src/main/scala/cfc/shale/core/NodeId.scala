package cfc.shale.core

import scalaz.BijectionT._
import scalaz.Scalaz._

case class NodeId(value: String)

object NodeId {

  implicit val stringBijection: Bijection[String, NodeId] =
    bijection[Id, Id, String, NodeId](NodeId(_), _.value)
}
