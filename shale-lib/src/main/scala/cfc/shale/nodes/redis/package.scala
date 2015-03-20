package cfc.shale
package nodes

import scalaz._, Scalaz._
import cfc.shale.redis.commands._
import cfc.shale.redis.containers._

import scala.collection.immutable.Set

package object redis {

  val nodeIds = RedisGetStringSet("node-ids").map(_.map(NodeId(_)))

  def nodeUrl(nodeId: NodeId) =
    RedisStringOption(s"node/${nodeId.value}/url")

  def addNodeWithUrl(url: String): RedisCommand[NodeId] =
    for {
      nodeId <- RedisCommand(generateNodeId())
      _ <- nodeUrl(nodeId).set(Some(url)).map(_ => nodeId)
    } yield nodeId

  def removeNode(nodeId: NodeId) =
    RedisDeleteKeyPrefix(s"node/${nodeId.value}/")

  def getNodeTags(nodeId: NodeId) =
    RedisGetStringSet(s"node/${nodeId.value}/tags")

  def getNode(nodeId: NodeId): RedisCommand[NodeInRedis] =
    RedisCommand.reduceUnordered(Stream(
      nodeUrl(nodeId).get.map(x => NodeInRedis(url=x)),
      getNodeTags(nodeId).map(x => NodeInRedis(tags=x))
    ))(Reducer.identityReducer)

  val getNodes: RedisCommand[Set[NodeInRedis]] =
    for {
      nodeIds <- nodeIds
      nodes <- RedisCommand.reduceUnorderedList[NodeInRedis](nodeIds.toSeq.map(getNode))
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
}