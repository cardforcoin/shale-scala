package cfc.shale

import cfc.shale.core.{NodeAddress, NodeId, ShaleSessionId}
import cfc.shale.redis_client.commands._
import cfc.shale.redis_client.containers._

import scala.collection.immutable.Set
import scalaz.Scalaz._
import scalaz._

package object redis {

  // Hack around a bug in IntelliJ
  import cfc.shale.redis.Node

  val nodeIds: RedisSet[NodeId] =
    RedisStringSet("node-ids").xmapb(NodeId.stringBijection)

  def nodeAddress(nodeId: NodeId): RedisContainer[Option[NodeAddress]] =
    RedisStringOption(s"node/${nodeId.value}/url")
      .xmapb(NodeAddress.stringBijection.liftInto[Option])

  def removeNode(nodeId: NodeId) =
    RedisCommand.gatherUnordered(Seq(
      RedisDeleteKeyPrefix(s"node/${nodeId.value}/"),
      nodeIds.remove(nodeId)
    ))

  def nodeTags(nodeId: NodeId) =
    RedisStringSet(s"node/${nodeId.value}/tags")

  def getNode(nodeId: NodeId): RedisCommand[Node] =
    RedisCommand.reduceUnordered(Stream(
      nodeAddress(nodeId).get.map(x => Node(address=x)),
      nodeTags(nodeId).get.map(x => Node(tags=x))
    ))(Reducer.identityReducer)

  val getNodes: RedisCommand[Set[Node]] =
    for {
      nodeIds <- nodeIds.get
      nodes <- RedisCommand.gatherUnordered[Node](nodeIds.toSeq.map(getNode))
    } yield nodes.toSet

  def getNodeIdToAddressMap: RedisCommand[Map[NodeId, Option[NodeAddress]]] =
    for {
      nodeIds <- nodeIds.get
      tuples <- RedisCommand.gatherUnordered[(NodeId, Option[NodeAddress])](
        nodeIds.toSeq.map(nodeId => nodeAddress(nodeId).get.map(address => (nodeId, address))))
    } yield tuples.toMap

  val getNodeAddresses: RedisCommand[Set[NodeAddress]] =
    for {
      nodeIds <- nodeIds.get
      addresses <- RedisCommand.gatherUnordered[Option[NodeAddress]](
        nodeIds.toSeq.map(nodeAddress(_).get))
    } yield addresses.flatMap(_.toStream).toSet

  val sessionIds: RedisSet[ShaleSessionId] =
    RedisStringSet("session-ids").xmapb(ShaleSessionId.stringBijection)

  def nodeId(sessionId: ShaleSessionId): RedisContainer[Option[NodeId]] =
    RedisStringOption(s"session/${sessionId.value}/node-id")
      .xmapb(NodeId.stringBijection.liftInto[Option])

  def getNodeAddress(sessionId: ShaleSessionId): RedisCommand[Option[NodeAddress]] =
    for {
      nodeIdOption <- nodeId(sessionId).get
      nodeAddressOption <- nodeIdOption match {
        case None =>
          RedisCommand.now(None)
        case Some(nodeId: NodeId) =>
          nodeAddress(nodeId).get
      }
    }
    yield nodeAddressOption

  def sessionTags(sessionId: ShaleSessionId) =
    RedisStringSet(s"session/${sessionId.value}/tags")

  def reserved(sessionId: ShaleSessionId) =
    RedisStringOption(s"session/${sessionId.value}/reserved")

  def currentUrl(sessionId: ShaleSessionId) =
    RedisStringOption(s"session/${sessionId.value}/current-url")

  def browserName(sessionId: ShaleSessionId) =
    RedisStringOption(s"session/${sessionId.value}/browser-name")

  def webDriverId(sessionId: ShaleSessionId) =
    RedisStringOption(s"session/${sessionId.value}/webdriver-id")

  def getSession(sessionId: ShaleSessionId):
      RedisCommand[Session] = {
    RedisCommand.reduceUnordered(Stream(
      nodeId(sessionId).get.map(x => Session(nodeId=x)),
      sessionTags(sessionId).get.map(x => Session(tags=x)),
      reserved(sessionId).get.map(x => Session(reserved=x)),
      currentUrl(sessionId).get.map(x => Session(currentUrl=x)),
      browserName(sessionId).get.map(x => Session(browserName=x)),
      webDriverId(sessionId).get.map(x => Session(webDriverId=x))
    ))(Reducer.identityReducer)
  }
}
