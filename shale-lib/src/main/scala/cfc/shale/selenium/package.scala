package cfc.shale

import argonaut.Argonaut.IListDecodeJson
import argonaut.DecodeJson
import org.http4s.Http4s.uri
import org.http4s.argonaut.jsonOf
import org.http4s.{EntityDecoder, Uri}

import scala.collection.immutable.Set
import scalaz.concurrent.Task

package object selenium {

  /** A Selenium session id. Note that this is different from a Shale session id. */
  case class SessionId(id: String)

  /** The set of session ids on a node. */
  case class NodeSessionIds(ids: Set[SessionId])

  implicit val NodeSessionIdsDecodeJson: DecodeJson[NodeSessionIds] =
    DecodeJson(_.downField("value").as(
      IListDecodeJson(DecodeJson(_.downField("id").as[String]))
        .map(_.toList.map(SessionId).toSet)
    )).map(NodeSessionIds)

  implicit val NodeSessionIdsEntityDecoder: EntityDecoder[NodeSessionIds] =
    jsonOf(NodeSessionIdsDecodeJson)

  def getSessionIdsForNodeUrl(nodeUrl: Uri): Task[NodeSessionIds] =
    http_client.get[NodeSessionIds](nodeUrl.resolve(uri("sessions/")))
}
