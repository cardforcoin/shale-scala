package cfc.shale

import cfc.shale.lib.Shale
import org.http4s.server.HttpService
import org.http4s.dsl._

package object http_server {

  def createService(shale: Shale) = HttpService {
    case GET -> Root => Ok("test")
  }
}
