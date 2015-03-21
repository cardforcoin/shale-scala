package cfc.shale

import org.http4s.Status.ResponseClass.Successful
import org.http4s.{EntityDecoder, Uri}

import scalaz.concurrent.Task

package object http_client {

  val client = org.http4s.client.blaze.defaultClient

  def get[A](uri: Uri)(implicit decoder: EntityDecoder[A]): Task[A] =
    client(uri).flatMap({
      case Successful(response) =>
        response.as[A].handleWith({ case t => BadHttpResponse.task(response, Some(t)) })
      case response =>
        BadHttpResponse.task(response)
    })
}
