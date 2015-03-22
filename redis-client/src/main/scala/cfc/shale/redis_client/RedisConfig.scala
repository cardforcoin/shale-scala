package cfc.shale.redis_client

import com.typesafe.config.Config

case class RedisConfig(
  host: String,
  port: Int,
  db: Int,
  keyPrefix: String
)

object RedisConfig {

  def parse(config: Config): RedisConfig =
    RedisConfig(
      host = config.getString("host"),
      port = config.getInt("port"),
      db = config.getInt("db"),
      keyPrefix = config.getString("key-prefix")
    )
}
