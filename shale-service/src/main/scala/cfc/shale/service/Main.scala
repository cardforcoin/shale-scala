package cfc.shale.service

import cfc.shale.Shale
import org.http4s.server.blaze.BlazeBuilder

object Main {

  def main(args: Array[String]): Unit = {

    val config = loadConfig
    cfc.shale.logback.init(config.getString("cfc.shale.log-file"))
    val shaleServiceConfig = ShaleServiceConfig.parse(config)
    val redis = cfc.shale.redis.getRedis(shaleServiceConfig.redis)
    val shale = Shale(redis)

    BlazeBuilder
      .bindHttp(port=shaleServiceConfig.http.port, host=shaleServiceConfig.http.host)
      .mountService(cfc.shale.http_server.createService(shale), "").run.awaitShutdown()
  }
}
