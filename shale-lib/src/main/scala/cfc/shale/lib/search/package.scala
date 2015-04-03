package cfc.shale
package lib

import cfc.shale.core.ShaleSessionId
import cfc.shale.redis_client.commands.RedisCommand

package object search {

  def getTargets: RedisCommand[Seq[SessionMatchTarget]] =
    for {
      sessionIds <- redis.sessionIds.get
      targets <- RedisCommand.gatherUnordered(sessionIds.toSeq.map(getTarget))
    }
    yield targets

  def getTarget(sessionId: ShaleSessionId): RedisCommand[SessionMatchTarget] =
    ???
//    for {
//      session: SessionInRedis <- sessions.redis.getSession(sessionId)
//      node <- session.nodeId match {
//        case None => RedisCommand.now(NodeInRedis())
//        case Some(nodeId) => nodes.redis.getNode(nodeId)
//      }
//    } yield SessionMatchTarget(sessionId, session, node)
}
