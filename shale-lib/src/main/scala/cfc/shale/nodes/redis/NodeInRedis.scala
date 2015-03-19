package cfc.shale.nodes.redis

import scalaz.Monoid

case class NodeInRedis(
  url: Option[String] = None,
  tags: Set[String] = Set.empty
)

object NodeInRedis {

  implicit val monoid = new Monoid[NodeInRedis] {

    override def zero = NodeInRedis()

    override def append(f1: NodeInRedis, f2: => NodeInRedis): NodeInRedis =
      NodeInRedis(
        url = f2.url.orElse(f1.url),
        tags = f1.tags ++ f2.tags
      )
  }
}
