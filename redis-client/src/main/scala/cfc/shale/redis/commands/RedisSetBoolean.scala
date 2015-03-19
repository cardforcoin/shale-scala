package cfc.shale.redis.commands

object RedisSetBoolean {

  def apply(key: String, value: Boolean): RedisCommand[Unit] =
    RedisSetString(key, value match {
      case false => "0"
      case true => "1"
    })
}
