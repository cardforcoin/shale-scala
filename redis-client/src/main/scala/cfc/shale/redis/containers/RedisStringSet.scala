package cfc.shale.redis.containers

import cfc.shale.redis.commands.RedisGetStringSet

import scala.collection.immutable.Set

case class RedisStringSet(key: String) extends RedisGetter[Set[String]] {

  override def getCommand =
    RedisGetStringSet(key).map(_.getOrElse(Set.empty).flatMap(_.toSet))
}
