package cfc.shale
package sessions

import cfc.shale.nodes.NodeId
import cfc.shale.redis.Redis
import cfc.shale.redis.commands.{RedisCommand, RedisGetStringSet}
import cfc.shale.redis.containers.RedisContainer.BijectionLifter
import cfc.shale.redis.containers.{RedisContainer, RedisStringOption}

import scalaz._, Scalaz._

package object redis {

  val getSessionIds = RedisGetStringSet("session-ids").map(_.map(SessionId(_)))

  def nodeId(sessionId: SessionId): RedisContainer[Option[NodeId]] =
    RedisStringOption(s"session/${sessionId.value}/node-id")
      .biject[Option[NodeId]](NodeId.stringBijection.liftInto[Option])

  def getNodeUrl(sessionId: SessionId): RedisCommand[Option[String]] =
    for {
      nodeIdOption <- nodeId(sessionId).get
      nodeUrlOption <- nodeIdOption match {
        case None =>
          RedisCommand.now(None)
        case Some(nodeId: NodeId) =>
          nodes.redis.nodeUrl(nodeId).get
      }
    }
    yield nodeUrlOption

  def getTags(sessionId: SessionId)(implicit redis: Redis) =
    RedisGetStringSet(s"session/${sessionId.value}/tags")

  def reserved(sessionId: SessionId)(implicit redis: Redis) =
    RedisStringOption(s"session/${sessionId.value}/reserved")

  def currentUrl(sessionId: SessionId)(implicit redis: Redis) =
    RedisStringOption(s"session/${sessionId.value}/current-url")

  def browserName(sessionId: SessionId)(implicit redis: Redis) =
    RedisStringOption(s"session/${sessionId.value}/browser-name")

  def getSession(sessionId: SessionId)(implicit redis: Redis):
      RedisCommand[SessionInRedis] = {
    RedisCommand.reduceUnordered(Stream(
      nodeId(sessionId).get.map(x => SessionInRedis(nodeId=x)),
      getTags(sessionId).map(x => SessionInRedis(tags=x)),
      reserved(sessionId).get.map(x => SessionInRedis(reserved=x)),
      currentUrl(sessionId).get.map(x => SessionInRedis(currentUrl=x)),
      browserName(sessionId).get.map(x => SessionInRedis(browserName=x))
    ))(Reducer.identityReducer)
  }
}
