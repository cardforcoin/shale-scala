package cfc.shale

import cfc.shale.core.{NodeId, ShaleSessionId}
import cfc.shale.redis_client.commands._
import cfc.shale.redis_client.containers.RedisContainer.BijectionLifter
import cfc.shale.redis_client.containers._

import scala.collection.immutable.Set
import scalaz.Scalaz._
import scalaz._

package object redis {

  // Hack around a bug in IntelliJ
  import cfc.shale.redis.Node

  val nodeIds = RedisGetStringSet("node-ids").map(_.map(NodeId(_)))

  def nodeUrl(nodeId: NodeId) =
    RedisStringOption(s"node/${nodeId.value}/url")

  def addNodeWithUrl(url: String): RedisCommand[NodeId] =
    for {
      nodeId <- RedisCommand(NodeId.random())
      _ <- nodeUrl(nodeId).set(Some(url)).map(_ => nodeId)
    } yield nodeId

  def removeNode(nodeId: NodeId) =
    RedisDeleteKeyPrefix(s"node/${nodeId.value}/")

  def getNodeTags(nodeId: NodeId) =
    RedisGetStringSet(s"node/${nodeId.value}/tags")

  def getNode(nodeId: NodeId): RedisCommand[Node] =
    RedisCommand.reduceUnordered(Stream(
      nodeUrl(nodeId).get.map(x => Node(url=x)),
      getNodeTags(nodeId).map(x => Node(tags=x))
    ))(Reducer.identityReducer)

  val getNodes: RedisCommand[Set[Node]] =
    for {
      nodeIds <- nodeIds
      nodes <- RedisCommand.reduceUnorderedList[Node](nodeIds.toSeq.map(getNode))
    } yield nodes.toSet

  def getNodeIdToUrlMap: RedisCommand[Map[NodeId, Option[String]]] =
    for {
      nodeIds <- nodeIds
      tuples <- RedisCommand.reduceUnorderedList[(NodeId, Option[String])](
        nodeIds.toSeq.map(nodeId => nodeUrl(nodeId).get.map(url => (nodeId, url))))
    } yield tuples.toMap

  val getNodeUrls: RedisCommand[Set[String]] =
    for {
      nodeIds <- nodeIds
      urls <- RedisCommand.reduceUnorderedList[Option[String]](
        nodeIds.toSeq.map(nodeUrl(_).get))
    } yield urls.flatMap(_.toStream).toSet

  /**
   * Updates the node list in redis to match the node pool.
   */
  def setNodeUrls(nodePoolUrls: Set[String]): RedisCommand[Unit] =
    for (idToUrl <- getNodeIdToUrlMap)
    yield {
      val urlToId = idToUrl.collect({ case (nodeId, Some(url)) => (url, nodeId) })
      val redisUrls = urlToId.keySet
      RedisCommand.reduceUnordered[Unit, Unit](
        (
          for (url <- (nodePoolUrls -- redisUrls).toSeq)
          yield addNodeWithUrl(url).map(_ => ())
        ) ++ (
          for (url <- (redisUrls -- nodePoolUrls).toSeq)
          yield removeNode(urlToId(url)).map(_ => ())
        )
      )
    }
  val getSessionIds = RedisGetStringSet("session-ids").map(_.map(ShaleSessionId(_)))

  def nodeId(sessionId: ShaleSessionId): RedisContainer[Option[NodeId]] =
    RedisStringOption(s"session/${sessionId.value}/node-id")
      .biject[Option[NodeId]](NodeId.stringBijection.liftInto[Option])

  def getNodeUrl(sessionId: ShaleSessionId): RedisCommand[Option[String]] =
    for {
      nodeIdOption <- nodeId(sessionId).get
      nodeUrlOption <- nodeIdOption match {
        case None =>
          RedisCommand.now(None)
        case Some(nodeId: NodeId) =>
          nodeUrl(nodeId).get
      }
    }
    yield nodeUrlOption

  def getSessionTags(sessionId: ShaleSessionId) =
    RedisGetStringSet(s"session/${sessionId.value}/tags")

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
      getSessionTags(sessionId).map(x => Session(tags=x)),
      reserved(sessionId).get.map(x => Session(reserved=x)),
      currentUrl(sessionId).get.map(x => Session(currentUrl=x)),
      browserName(sessionId).get.map(x => Session(browserName=x)),
      webDriverId(sessionId).get.map(x => Session(webDriverId=x))
    ))(Reducer.identityReducer)
  }
}
