package cfc.shale.core

import java.util.UUID

import scalaz.BijectionT._
import scalaz.Scalaz._

case class NodeId(value: String)

object NodeId {

  def random(): NodeId = NodeId(UUID.randomUUID().toString)

  implicit val stringBijection: Bijection[String, NodeId] =
    bijection[Id, Id, String, NodeId](NodeId(_), _.value)
}
