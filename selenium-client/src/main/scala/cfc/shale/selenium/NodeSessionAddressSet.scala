package cfc.shale.selenium

import scala.collection.immutable.Set
import scalaz.concurrent.Task

case class NodeSessionAddressSet(addresses: Set[SeleniumSessionAddress])

object NodeSessionAddressSet extends NodeSessionAddressSets

trait NodeSessionAddressSets {

  def getNodeSessionAddressSet(node: SeleniumNodeAddress): Task[NodeSessionAddressSet] =
    getNodeSessionIdSet(node).map(sessionIds =>
      NodeSessionAddressSet(sessionIds.ids.map(i => SeleniumSessionAddress(node, i))))
}
