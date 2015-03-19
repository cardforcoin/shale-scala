package cfc.shale

import com.redis.RedisClientPool

package object redis {

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
}
