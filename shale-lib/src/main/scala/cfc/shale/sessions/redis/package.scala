package cfc.shale
package sessions

import cfc.shale.core.{ShaleSessionId, NodeId}
import cfc.shale.redis.Session
import cfc.shale.redis_client.commands.{RedisCommand, RedisGetStringSet}
import cfc.shale.redis_client.containers.RedisContainer.BijectionLifter
import cfc.shale.redis_client.containers.{RedisContainer, RedisStringOption}

import scalaz._, Scalaz._

package object redis {

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
          nodes.redis.nodeUrl(nodeId).get
      }
    }
    yield nodeUrlOption

  def getTags(sessionId: ShaleSessionId) =
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
      getTags(sessionId).map(x => Session(tags=x)),
      reserved(sessionId).get.map(x => Session(reserved=x)),
      currentUrl(sessionId).get.map(x => Session(currentUrl=x)),
      browserName(sessionId).get.map(x => Session(browserName=x)),
      webDriverId(sessionId).get.map(x => Session(webDriverId=x))
    ))(Reducer.identityReducer)
  }
}
