package cfc.shale.nodes

import scalaz._, Scalaz._, BijectionT._

case class NodeId(value: String)

object NodeId {

  implicit val stringBijection: Bijection[String, NodeId] =
    bijection[Id, Id, String, NodeId](NodeId(_), _.value)
}
