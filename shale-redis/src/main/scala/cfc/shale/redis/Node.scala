package cfc.shale.redis

import cfc.shale.core.NodeAddress

import scalaz.Monoid

case class Node(
  address: Option[NodeAddress] = None,
  tags: Set[String] = Set.empty
)

object Node {

  implicit val monoid = new Monoid[Node] {

    override def zero = Node()

    override def append(f1: Node, f2: => Node): Node =
      Node(
        address = f2.address.orElse(f1.address),
        tags = f1.tags ++ f2.tags
      )
  }
}
