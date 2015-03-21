package cfc.shale.selenium

import scala.collection.immutable.Set
import scalaz.concurrent.Task

case class NodeSessionAddressSet(addresses: Set[SessionAddress])

object NodeSessionAddressSet extends NodeSessionAddressSets

trait NodeSessionAddressSets {

  def getNodeSessionAddressSet(node: NodeAddress): Task[NodeSessionAddressSet] =
    getNodeSessionIdSet(node).map(sessionIds =>
      NodeSessionAddressSet(sessionIds.ids.map(i => SessionAddress(node, i))))
}
