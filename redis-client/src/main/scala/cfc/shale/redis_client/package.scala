package cfc.shale

import com.redis.serialization.Format
import com.redis.{RedisClient, RedisClientPool}
import org.joda.time.Duration

package object redis_client {

  def getRedisClients(config: RedisConfig): RedisClientPool =
    new RedisClientPool(
      host = config.host,
      port = config.port,
      database = config.db
    )

  def getRedis(config: RedisConfig): Redis =
    Redis(
      clients = getRedisClients(config),
      keyPrefix = config.keyPrefix
    )

  implicit class ExtendedClient(client: RedisClient) {

    def psetex(key: Any, expiry: Int, value: Any)(implicit format: Format): Boolean =
      client.send("PSETEX", List(key, expiry, value))(client.asBoolean)

    def setex(key: Any, expiry: Duration, value: Any)(implicit format: Format): Boolean =
      DurationInt.orThrow(expiry) match {
        case MillisDuration(millis) => client.psetex(key, millis, value)
        case SecondsDuration(seconds) => client.setex(key, seconds, value)
      }

    def setex(key: Any, expiry: Option[Duration], value: Any)(implicit format: Format): Boolean =
      expiry match {
        case Some(d) => client.setex(key, d, value)
        case None => client.set(key, value)
      }
  }
}
