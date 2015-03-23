package cfc.shale.redis_client
package commands

import org.joda.time.Duration

object RedisSetBoolean {

  def apply(key: String, value: Boolean, expiry: Option[Duration] = None): RedisCommand[Unit] =
    RedisSetString(
      key = key,
      value = value match {
        case false => "0"
        case true => "1"
      },
      expiry = expiry
    )
}
