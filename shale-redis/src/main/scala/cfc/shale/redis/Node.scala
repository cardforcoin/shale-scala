package cfc.shale.redis

import scalaz.Monoid

case class Node(
  url: Option[String] = None,
  tags: Set[String] = Set.empty
)

object Node {

  implicit val monoid = new Monoid[Node] {

    override def zero = Node()

    override def append(f1: Node, f2: => Node): Node =
      Node(
        url = f2.url.orElse(f1.url),
        tags = f1.tags ++ f2.tags
      )
  }
}
