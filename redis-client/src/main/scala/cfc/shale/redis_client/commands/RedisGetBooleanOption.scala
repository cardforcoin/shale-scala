package cfc.shale.redis_client.commands

object RedisGetBooleanOption {

  def apply(key: String): RedisCommand[Option[Boolean]] =
    RedisGetStringOption(key).map({
      case Some("0") => Some(false)
      case Some("1") => Some(true)
      case _ => None
    })
}
