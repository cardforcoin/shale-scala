package cfc.shale.sessions.redis

case class SessionInRedis(
  tags: Set[String],
  reserved: Option[String],
  currentUrl: Option[String],
  browserName: Option[String],
  nodeId: Option[String]
)

object SessionInRedis {
  val blank = SessionInRedis(Set.empty, None, None, None, None)
}
