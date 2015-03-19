package cfc.shale

import java.util.concurrent.TimeUnit

import cfc.shale.redis.RedisConfig
import com.typesafe.config.Config

import scala.concurrent.duration._

case class ShaleConfig(
  config: Config,
  http: ShaleConfig.HttpConfig,
  redis: RedisConfig,
  sessionRefresh: ShaleConfig.RefreshTiming
)

object ShaleConfig {

  def parse(config: Config) = {
    val x = config.getConfig("cfc.shale")
    ShaleConfig(
      config = config,
      http = HttpConfig.parse(x.getConfig("http")),
      redis = RedisConfig.parse(x.getConfig("redis")),
      sessionRefresh = RefreshTiming.parse(x.getConfig("session-refresh"))
    )
  }

  def parseDuration(config: Config, key: String) =
    config.getDuration(key, TimeUnit.MILLISECONDS).millis

  case class HttpConfig(host: String, port: Int)

  object HttpConfig {

    def parse(config: Config) =
      HttpConfig(config.getString("host"), config.getInt("port"))
  }

  case class RefreshTiming(timeout: FiniteDuration, interval: FiniteDuration)

  object RefreshTiming {

    def parse(config: Config) =
      RefreshTiming(
        timeout = parseDuration(config, "timeout"),
        interval = parseDuration(config, "interval")
      )
  }
}
