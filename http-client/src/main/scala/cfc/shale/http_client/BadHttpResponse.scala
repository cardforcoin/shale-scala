package cfc.shale.http_client

import org.http4s.{Response, Status}

import scalaz.Show
import scalaz.concurrent.Task

import BadHttpResponse.ShowBadHttpResponse

case class BadHttpResponse(
  status: Status,
  body: String,
  cause: Option[Throwable] = None
) extends Exception {

  override def getMessage: String = Show[BadHttpResponse].shows(this)
}

object BadHttpResponse {

  def task(response: Response, cause: Option[Throwable] = None): Task[Nothing] =
    response.as[String].map(body =>
      throw BadHttpResponse(response.status, body, cause))

  implicit val ShowBadHttpResponse: Show[BadHttpResponse] =
    Show.shows((x: BadHttpResponse) =>
      (
        x.cause.flatMap(cause => Option(cause.getMessage)).toSeq
          ++ Seq(x.status.code.toString, x.body)
      ).mkString("\n\n")
    )
}
