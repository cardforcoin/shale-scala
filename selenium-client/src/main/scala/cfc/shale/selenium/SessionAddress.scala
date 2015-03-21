package cfc.shale.selenium

import scalaz.concurrent.Task

case class SessionAddress(node: NodeAddress, session: SessionId)

object SessionAddress extends SessionAddresses

trait SessionAddresses {
  
  def getSessionAddressSet(node: NodeAddress): Task[Set[SessionAddress]] =
    getNodeSessionIdSet(node).map(_.ids.map(i => SessionAddress(node, i)))
}
