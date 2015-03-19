package cfc.shale.nodes.redis

case class NodeInRedis(
  url: Option[String],
  tags: Set[String]
)

object NodeInRedis {
  val blank = NodeInRedis(None, Set.empty)
}
