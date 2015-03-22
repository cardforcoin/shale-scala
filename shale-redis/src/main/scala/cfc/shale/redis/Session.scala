package cfc.shale.redis

import cfc.shale.core.NodeId

import scalaz.Monoid

case class Session(
  tags: Set[String] = Set.empty,
  reserved: Option[String] = None,
  currentUrl: Option[String] = None,
  browserName: Option[String] = None,
  nodeId: Option[NodeId] = None,
  webDriverId: Option[String] = None
)

object Session {

  implicit val monoid = new Monoid[Session] {

    override def zero = Session()

    override def append(f1: Session, f2: => Session): Session =
      Session(
        tags = f1.tags ++ f2.tags,
        reserved = f2.reserved orElse f1.reserved,
        currentUrl = f2.currentUrl orElse f1.currentUrl,
        browserName = f2.browserName orElse f1.browserName,
        nodeId = f2.nodeId orElse f1.nodeId,
        webDriverId = f2.webDriverId orElse f1.webDriverId
      )
  }
}
