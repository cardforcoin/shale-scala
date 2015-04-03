package cfc.shale

import akka.actor.ActorSystem
import akka.util.Timeout
import cfc.shale.core.{ShaleSessionId, NodeAddress}
import cfc.shale.redis_client.Redis
import cfc.shale.redis_client.commands.RedisCommand

import scala.concurrent.Future
import scalaz.Reducer
import scalaz.concurrent.Task
import scala.collection.immutable.Set

package object sessions {

  def refreshSessions(implicit redis: Redis): Task[Unit] =
    for {
      nodeUrls <- cfc.shale.redis.getNodeAddresses.task
      _ <- Task.reduceUnordered(nodeUrls.toSeq.map(refreshSessionsForNode))(Reducer.ListReducer)
    } yield ()

  def refreshSessionsForNode(address: NodeAddress)(implicit redis: Redis): Task[Unit] =
    ???
//    for {
//      sessionIds <- selenium.getSessionIdsForNodeUrl(nodeUrl)
//      _ <- Future.sequence(sessionIds.toSeq.map(refreshSession))
//    } yield ()

  def refreshSession(sessionId: ShaleSessionId)(implicit redis: Redis,
      system: ActorSystem): Future[Unit] =
    ???
//  {
//    import system.dispatcher
//    for {
//      nodeIdOption <- sessions.redis.getNodeId(sessionId)
//      nodeUrlOption <- taskOptionFlatMap(nodeIdOption)(nodes.redis.nodeUrl)
//      _ <- taskFlatMap(nodeUrlOption)(nodeUrl =>
//        refreshSession(sessionId=sessionId, nodeUrl=nodeUrl))
//    } yield ()
//  }

  def refreshSession(sessionId: ShaleSessionId, nodeAddress: NodeAddress)
      (implicit redis: Redis, system: ActorSystem): Future[Unit] =
    ???
}
