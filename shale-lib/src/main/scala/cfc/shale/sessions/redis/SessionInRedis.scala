package cfc.shale.sessions.redis

import cfc.shale.nodes.NodeId

import scalaz.Monoid

case class SessionInRedis(
  tags: Set[String] = Set.empty,
  reserved: Option[String] = None,
  currentUrl: Option[String] = None,
  browserName: Option[String] = None,
  nodeId: Option[NodeId] = None,
  webDriverId: Option[String] = None
)

object SessionInRedis {

  implicit val monoid = new Monoid[SessionInRedis] {

    override def zero = SessionInRedis()

    override def append(f1: SessionInRedis, f2: => SessionInRedis): SessionInRedis =
      SessionInRedis(
        tags = f1.tags ++ f2.tags,
        reserved = f2.reserved orElse f1.reserved,
        currentUrl = f2.currentUrl orElse f1.currentUrl,
        browserName = f2.browserName orElse f1.browserName,
        nodeId = f2.nodeId orElse f1.nodeId,
        webDriverId = f2.webDriverId orElse f1.webDriverId
      )
  }
}
