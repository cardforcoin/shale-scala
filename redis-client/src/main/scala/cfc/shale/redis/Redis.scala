package cfc.shale.redis

import com.redis.RedisClientPool

case class Redis(clients: RedisClientPool, keyPrefix: String) {

  def prefix(key: String): String = Seq(keyPrefix, key).mkString("/")
}
