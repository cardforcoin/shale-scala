package cfc.shale.redis_client.commands

import scala.collection.immutable.Set

object RedisGetStringSet {

  def apply(key: String): RedisCommand[Set[String]] =
    RedisGetStringOptionSetOption(key).map(_.getOrElse(Set.empty).flatMap(_.toSet))
}
