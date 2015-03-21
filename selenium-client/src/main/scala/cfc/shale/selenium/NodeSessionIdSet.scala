package cfc.shale.selenium

import argonaut.Argonaut._
import argonaut.DecodeJson
import cfc.shale.http_client
import org.http4s.EntityDecoder
import org.http4s.Http4s._
import org.http4s.argonaut.jsonOf

import scala.collection.immutable.Set
import scalaz.concurrent.Task

/** The set of session ids on a node. */
case class NodeSessionIdSet(ids: Set[SessionId])

object NodeSessionIdSet extends NodeSessionIdSets

trait NodeSessionIdSets {

  implicit val NodeSessionIdsDecodeJson: DecodeJson[NodeSessionIdSet] =
    DecodeJson(_.downField("value").as(
      IListDecodeJson(DecodeJson(_.downField("id").as[String]))
        .map(_.toList.map(SessionId).toSet)
    )).map(NodeSessionIdSet.apply)

  implicit val NodeSessionIdsEntityDecoder: EntityDecoder[NodeSessionIdSet] =
    jsonOf(NodeSessionIdsDecodeJson)

  def getNodeSessionIdSet(node: NodeAddress): Task[NodeSessionIdSet] =
    http_client.get[NodeSessionIdSet](node.uri.resolve(uri("sessions/")))
}